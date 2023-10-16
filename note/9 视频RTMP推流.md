### RTMP推流

[项目地址](https://gitee.com/guaishoun/rtmp_dump_pusher)

include <> 系统目录找

include_derectories 相当于把指定目录添加为系统查找目录；

```C++
//C组合字符和数字
char ver[70];
sprintf(ver,"rtmp version: %d",RTMP_LibVersion());
return env->NewStringUTF(ver);
```

```
rtmp.h里定义了NO_CRYPTO，可以避免编译错误openssl
# -L 库查找路径 -D 定义宏
# 目前是纯C环境 用CMAKE_C_FLAGS
在cmake中set(CMAKE_C_FLAGS   "${CMAKE_CXX_FLAGS}   -DNO_CRYPTO")
```

添加x264库

rtmp.h和x264.h已经自动添加了extern “C”，比ffmpeg友好

```
addsubliabry()
要记得修改gradle
```

```
RTMP视频数据格式与FLV格式相似
视频：Camera采集->H264打包->RTMP包->发送
音频：AudioRecord采集->AAC打包->RTMP包->发送

什么是sps参数集序列和pps图像集

NALU网络抽象层，H264是火车NALU是车厢
IDR是关键帧，遇到IDR帧前面的队列立刻被清空
```

```
#java层
CameraHelper
```

### 视频采集封装

1 获取媒体数据（视频/音频）

​	x.264编码器：YUV->H.264

2封装数据（打包压缩数据）RTMP_Packet

3发送到服务器

```
初始化视频编码器、设置w h fps bitrat

1 rtmp初始化
2 设置流媒体地址
3 开始输出模式
4 建立连接
5 连接流
6 记录开始推流的时间
7 发送数据包




FlvAnalyser
```





#### 音频推流

