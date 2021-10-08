# rabbit rpc batch SpringBoot实现

用于消息队列批处理框架的客户端接口实现。

## 环境
+ rabbitmq安装
+ java8

## 配置信息
+ rabbitmq配置(src/main/resources/application.properties)
+ 阿里云OSS权限信息（src/main/resources/oss.properties 可选，不配置也能调用）

## 接口信息

请查看类 `src/main/java/com/chenc/amqptest/module/se/controller/SEController.java`,接口相关模块例如SE、ASR都在module文件夹下。

## OTHER

远程JVM虚拟机调试例子，基于visualJVM
```
java -jar  -Dcom.sun.management.jmxremote.port=11406 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=192.168.1.106   amqptest-0.0.1-SNAPSHOT.jar
```