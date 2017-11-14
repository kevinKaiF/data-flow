# data-flow

data-flow用于解决mysql和oracle数据同步问题，mysql底层使用alibaba的canal二次开发，oracle数据同步使用物化日志。

## 设计理念
将每种类型的数据同步设计为一个有生命周期的实例instance，data-flow可以管理实例instance的生命周期

## 支持功能：
* 支持mysql数据同步到不同的系统，比如mysql,oracle,kafka,activemq等等
* 支持transform功能，可以按照自己的需求转换字段，库名，表名等
* 支持自定义处理逻辑
* 支持生产者和消费者两种实例，生产者实例目前只支持mysql,oracle（增量），消费者实例支持kafka,activemq,后续可扩展
* 以集群的方式运行实例，支持failover
