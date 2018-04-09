package com.github.dataflow.node.model.instance.mysql;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.otter.canal.common.utils.JsonUtils;
import com.alibaba.otter.canal.common.zookeeper.ZkClientx;
import com.alibaba.otter.canal.common.zookeeper.ZookeeperPathUtils;
import com.alibaba.otter.canal.parse.index.ZooKeeperLogPositionManager;
import com.alibaba.otter.canal.protocol.position.LogPosition;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * 修改原JSON序列化InetAddress的方式，使用{@link InetAddress#getHostAddress()}代替原来ZooKeeperLogPositionManager的{@link InetAddress#getHostName()}
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/11/7
 */
public class ZKLogPositionManager extends ZooKeeperLogPositionManager {
    private ZkClientx zkClientx;

    public ZKLogPositionManager() {
    }

    @SuppressWarnings("deprecation")
    public void start() {
        super.start();
        Assert.notNull(this.zkClientx);
    }

    public void stop() {
        super.stop();
    }

    public LogPosition getLatestIndexBy(String destination) {
        String path = ZookeeperPathUtils.getParsePath(destination);
        byte[] data = (byte[])this.zkClientx.readData(path, true);
        return data != null && data.length != 0?(LogPosition) JsonUtils.unmarshalFromByte(data, LogPosition.class):null;
    }

    /**
     * 解决canal序列化时使用hostName时，异活后无法解析hostName的问题
     *
     * @param destination
     * @param logPosition
     */
    public void persistLogPosition(String destination, LogPosition logPosition) {
        String path = ZookeeperPathUtils.getParsePath(destination);
        ObjectSerializer serializer = new InetAddressSerializer();
        SerializeConfig.getGlobalInstance().put(InetAddress.class, serializer);
        SerializeConfig.getGlobalInstance().put(Inet4Address.class, serializer);
        SerializeConfig.getGlobalInstance().put(Inet6Address.class, serializer);
        byte[] data = JsonUtils.marshalToByte(logPosition);
        try {
            this.zkClientx.writeData(path, data);
        } catch (ZkNoNodeException var6) {
            this.zkClientx.createPersistent(path, data, true);
        }

    }

    public void setZkClientx(ZkClientx zkClientx) {
        super.setZkClientx(zkClientx);
        this.zkClientx = zkClientx;
    }

    /**
     * 处理InetAddress的JSON序列化问题
     */
    public static class InetAddressSerializer implements ObjectSerializer {
        public static JsonUtils.InetAddressSerializer instance = new JsonUtils.InetAddressSerializer();

        public InetAddressSerializer() {
        }

        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            if(object == null) {
                serializer.writeNull();
            } else {
                InetAddress address = (InetAddress)object;
                serializer.write(address.getHostAddress());
            }
        }
    }
}
