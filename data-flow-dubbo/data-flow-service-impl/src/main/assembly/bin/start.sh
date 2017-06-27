#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf


# =======================================================================================
# 检测操作系统类型
# =======================================================================================
OS=`uname -s | tr [:upper:] [:lower:] | tr -d [:blank:]`
case "$OS" in
    'sunos')
        # OS="solaris"
        ;;
    'hp-ux' | 'hp-ux64') # 未经过验证
        # OS="linux"
        ;;
    'darwin') # Mac OSX
        OS="unix"
        ;;
    'unix_sv')
        OS="unix"
        ;;
esac
# 该脚本目前只支持linux、Mac OSX
if [ "$OS" != "linux" ] && [ "$OS" != "unix" ]; then
    echo "Unsupported OS: $OS"
    exit 1
fi


# =======================================================================================
# 检测服务是否已经启动，或者端口号是否已经被占用
# Mac OSX支持: ps -e -o 'pid=,command='，但linux必须写成: ps -e -o 'pid=' -o 'command='
# =======================================================================================
PIDS=`ps -e -o 'pid=' -o 'command='|grep java|grep "$CONF_DIR"|awk '{print $1}'`
if [ -n "$PIDS" ]; then
    # 服务已经启动
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi
if [ -n "$SERVER_PORT" ]; then
    # 端口号是否被占用
    # netstat的输出格式: 
    # linux:10.9.10.178:10050
    # Mac OSX: 192.168.169.5.56508
    if [ "$OS" == "unix" ]; then
        SERVER_PORT_COUNT=`netstat -ant -p tcp|tail -n +3|awk '{print $4}'|grep '[.:]$SERVER_PORT' -c`
    else
        SERVER_PORT_COUNT=`netstat -ant|tail -n +3|awk '{print $4}'|grep '[.:]$SERVER_PORT' -c`
    fi
    if [ $SERVER_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"
        exit 1
    fi
fi


# =======================================================================================
# 启动服务
# =======================================================================================
# dubbo服务配置参数
SERVER_NAME=`sed '/^#/d;/dubbo.application.name/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi
SERVER_PORT=`sed '/^#/d;/dubbo.protocol.port/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
SERVER_HOST=`sed '/^#/d;/dubbo.protocol.host/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
if [ -z "$SERVER_HOST" ]; then
    SERVER_HOST=127.0.0.1
fi

# 日志：log4j.xml文件路径、日志路径、stdout日志文件名
LOG4J_XML=`sed '/^#/d;/prop.log.log4j-xml/!d;s/.*=//' conf/log.properties | tr -d '\r'`
LOG_DIR=`sed '/^#/d;/prop.log.dir/!d;s/.*=//' conf/log.properties | tr -d '\r'`
if [ -n "$LOG_DIR" ]; then
    LOG_DIR=`dirname $LOG_DIR/stdout.log`
else
    LOG_DIR=$DEPLOY_DIR/logs
fi
if [ ! -d $LOG_DIR ]; then
    # 日志目录不存在，创建这个目录
    mkdir -p $LOG_DIR
fi
LOG_STDOUT=`sed '/^#/d;/prop.log.stdout-file/!d;s/.*=//' conf/log.properties | tr -d '\r'`
if [ -z "$LOG_STDOUT" ]; then
    LOG_STDOUT=$LOG_DIR/stdout.log
else
    LOG_STDOUT=$LOG_DIR/$LOG_STDOUT
fi

# classpath设置
LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
CLASS_PATH=$CONF_DIR:$LIB_JARS

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dlog4j.configuration=$LOG4J_XML "
#JAVA_MEM_OPTS=`sed '/^#/d;/java.jvm.mem-opts/!d;s/.*=//' conf/dubbo.properties | tr -d '\r'`
JAVA_MEM_OPTS="-server -Xms1024m -Xmx2048m -XX:ParallelGCThreads=2 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSFullGCsBeforeCompaction=10  -XX:PermSize=512M  -Djava.awt.headless=true"
if [ -z "$JAVA_HOME" ]; then
    export JAVA_HOME=/app/programs/jdk1.8.0_121
    export PATH=$JAVA_HOME/bin:$PATH
fi
echo "Starting the $SERVER_NAME, $SERVER_HOST:$SERVER_PORT"
# TODO: 未导入环境变量，远程启动会有问题
nohup java $JAVA_MEM_OPTS $JAVA_OPTS -classpath $CLASS_PATH com.alibaba.dubbo.container.Main > $LOG_STDOUT 2>&1 &


# =======================================================================================
# 检测服务状态，服务启动状态OK之后再退出
# =======================================================================================
echo -e "  Waiting for service status OK ...\c"
COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1 
    # 能够连通服务端口号，则服务启动完成
    COUNT=`echo status | nc -4 -i 1 $SERVER_HOST $SERVER_PORT | grep -c OK`
done
echo "OK!"
# 下面ps命令参数兼容linux、Mac OSX(Free BSD)
PIDS=`ps -e -o 'pid=' -o 'command='|grep java|grep "$CONF_DIR"|awk '{print $1}'`
echo "  PID: $PIDS"
echo "  STDOUT: $LOG_STDOUT" 
