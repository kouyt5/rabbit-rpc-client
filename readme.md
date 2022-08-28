# rabbit rpc batch SpringBoot实现
<a href="./LICENSE.txt"><img alt="GitHub" src="https://img.shields.io/github/license/kouyt5/rabbit-rpc-client"></a>
<a href="https://lgtm.com/projects/g/kouyt5/rabbit-rpc-client/alerts/"><img alt="Total alerts" src="https://img.shields.io/lgtm/alerts/g/kouyt5/rabbit-rpc-client.svg?logo=lgtm&logoWidth=18"/></a>
<a href="https://lgtm.com/projects/g/kouyt5/rabbit-rpc-client/context:java"><img alt="Language grade: Java" src="https://img.shields.io/lgtm/grade/java/g/kouyt5/rabbit-rpc-client.svg?logo=lgtm&logoWidth=18"/></a>

用于消息队列批处理框架的客户端接口实现。

## 环境
+ rabbitmq
+ java11
+ maven

## 如何运行

```bash
mvn package
java -jar target/rabbit-prc-client-1.0.0.jar
```

docker支持
```
docker build -f docker/Dockerfile . -t 511023/rabbit-rpc-client
docker run -it --rm --network host 511023/rabbit-rpc-client
```

## 接口信息

接口相关模块例如SE、ASR都在module文件夹下。
### 语音识别
+ 请求方法 `POST`
+ 接口地址: localhost:8083/asr
+ 接口参数:

|参数名|类型|示例|说明|
|---|---|---|---|
|audio|file|@/path/to/wav.wav|文件|
|audio|string|"wav"|音频格式, mp3等|

+ 输出:
```json
{
    "status": 200,
    "message": "success",
    "data": {
        "sentence": "xxxxx",
        "other": 200
    }
}
```

## OTHER

远程JVM虚拟机调试例子，基于visualJVM
```
java -jar  -Dcom.sun.management.jmxremote.port=11406 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=192.168.1.106   amqptest-0.0.1-SNAPSHOT.jar
```