# data-flow

data-flow用于解决mysql数据同步问题，底层使用alibaba的canal开发。

## 设计理念
将每种类型的数据同步设计为一个有生命周期的实例instance，data-flow可以管理实例instance的生命周期

## 支持功能：
* 支持mysql数据同步到不同的系统，比如mysql,oracle,kafka,activemq等等
* 支持transform功能，可以按照自己的需求转换字段，库名，表名等
* 支持生产者和消费者两种实例
* 以集群的方式运行实例，支持failover
