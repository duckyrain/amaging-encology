# JAVA常见技术研究

搞了这么多JAVA项目了，是时候开个坑了，汇总常见技术，不定期更新~~

## 模块介绍

便于理解，只对常用的一些技术进行举例，不做过多深入

### amaging-encology-common

提供一些常用的工具类，如：DateUtil, FtpUtil, HttpClientUtil, MailUtil等。支持配置文件参数定义（使用@Value注解实现）。

- HttpClientUtil配置示例：

```
httpclient.connectionRequestTimeout=1000
httpclient.connectionTimeout=5000
httpclient.socketTimeout=3000
httpclient.maxConnPerRoute=10
httpclient.maxConnTotal=1000
httpclient.connectionTimeToLive=30
httpclient.evictIdleConnections=30
```

- MailUtil配置示例：

```
mail.host=smtp.qiye.163.com
mail.port=25
mail.protocol=smtp
mail.from=duckyrain@xxx.com
mail.userName=duckyrain@xxx.com
mail.password=**********
```

### amaging-encology-dubbo



### amaging-encology-elasticsearch

基于springboot实现的es检索接口，包含：常规条件检索、全文检索等。

扩展参考：[《Msql作为数据源的ELK全文检索平台实现过程》](https://amaging.cn/2018/07/22/%E5%9F%BA%E4%BA%8EElasticSearch%E7%9A%84%E5%85%A8%E6%96%87%E6%A3%80%E7%B4%A2%E5%B9%B3%E5%8F%B0/ "Msql作为数据源的ELK全文检索平台实现过程")

### amaging-encology-io

BIO、NIO、AIO、Netty4使用示例。

### amaging-encology-mq

ActiveMq、RabbmitMq、RocketMq、Kafka客户端调用示例

### amaging-encology-mybatis

mybatis实现原理示例

### amaging-encology-redis

常用redis客户端示例，包含：单点、分片、哨兵等实现，开箱即用

### amaging-encology-rpc

远程过程调用（*Remote Process Call*）实现原理示例，微服务基础

### amaging-encology-spring-boot

springboot常见用法汇总

### amaging-encology-spring-cloud