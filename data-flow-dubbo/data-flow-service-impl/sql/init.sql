SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for data_instance
-- ----------------------------
DROP TABLE IF EXISTS `data_instance`;
CREATE TABLE `data_instance` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) NOT NULL COMMENT '实例名称',
  `SLAVE_ID` bigint(10) NOT NULL COMMENT '从库id',
  `HOST` varchar(20) NOT NULL COMMENT '数据库host',
  `PORT` int(5) NOT NULL COMMENT '数据库port',
  `JDBC_URL` varchar(100) NOT NULL COMMENT 'jdbc链接',
  `TYPE` int(1) NOT NULL COMMENT '数据源类型，1=MySQL,2=Oracle,3=POSTGRESQL,4=SQLSERVER',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(50) NOT NULL COMMENT '密码',
  `WHITE_FILTER` varchar(100) DEFAULT NULL COMMENT '白名单正则过滤表',
  `BLACK_FILTER` varchar(100) DEFAULT NULL COMMENT '黑名单正则过滤表',
  `TRANSFORM_SCRIPT` varchar(2000) DEFAULT NULL COMMENT '转换脚本',
  `OPTIONS` varchar(200) DEFAULT NULL COMMENT '其他配置，JSON格式',
  `NODE_PATH` varchar(100) DEFAULT NULL COMMENT '实例所在node的zk路径',
  `STATUS` int(1) DEFAULT NULL COMMENT '实例的状态,-1=正在创建，0=创建完成，1=启动状态，2=关停状态',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_output_mapping
-- ----------------------------
DROP TABLE IF EXISTS `data_output_mapping`;
CREATE TABLE `data_output_mapping` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `DATA_INSTANCE_ID` bigint(20) NOT NULL COMMENT 'DataInstance的主键',
  `SCHEMA_NAME` varchar(50) NOT NULL COMMENT '库名',
  `DATA_SOURCE_OUTPUT_ID` bigint(20) NOT NULL COMMENT '输出数据源的主键',
  `TOPIC` varchar(200) NOT NULL COMMENT '其他配置，JSON格式',
  PRIMARY KEY (`ID`),
  KEY `DOM_DATA_INSTANCE_ID_INDEX` (`DATA_INSTANCE_ID`) USING BTREE,
  KEY `DOM_DATA_SOURCE_OUTPUT_ID_INDEX` (`DATA_SOURCE_OUTPUT_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for data_source_output
-- ----------------------------
DROP TABLE IF EXISTS `data_source_output`;
CREATE TABLE `data_source_output` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TYPE` int(1) NOT NULL COMMENT '输出数据源的类型',
  `OPTIONS` varchar(200) NOT NULL COMMENT '输出数据源的配置，JSON格式',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_table
-- ----------------------------
DROP TABLE IF EXISTS `data_table`;
CREATE TABLE `data_table` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `DATA_INSTANCE_ID` bigint(20) NOT NULL COMMENT 'DataInstance的主键',
  `SCHEMA_NAME` varchar(50) NOT NULL COMMENT '库名',
  `TABLE_NAME` varchar(50) NOT NULL COMMENT '表名',
  `COLUMNS` varchar(2000) NOT NULL COMMENT '过滤的字段',
  PRIMARY KEY (`ID`),
  KEY `DT_DATA_INSTANCE_ID_INDEX` (`DATA_INSTANCE_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_node_config
-- ----------------------------
DROP TABLE IF EXISTS `data_node_config`;
CREATE TABLE `data_node_config` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TYPE` int(1) NOT NULL COMMENT '告警的类型',
  `OPTIONS` varchar(200) NOT NULL COMMENT '告警的配置，JSON格式',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_log
-- ----------------------------
DROP TABLE IF EXISTS `data_log`;
CREATE TABLE `data_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `INSTANCE_NAME` varchar(50) NOT NULL COMMENT '实例名称',
  `MESSAGE` varchar(3000) NOT NULL COMMENT '日志信息',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_user
-- ----------------------------
DROP TABLE IF EXISTS `data_user`;
CREATE TABLE `data_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USERNAME` varchar(20) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(100) NOT NULL COMMENT '密码',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `dataflow`.`data_user` (`USERNAME`, `PASSWORD`) VALUES ('admin', '123');

