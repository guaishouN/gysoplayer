# FFmpeg

#### 简介

未来+高新 。

[官网](https://www.ffmpeg.org/)    [中文翻译网](https://xdsnet.gitbooks.io/other-doc-cn-ffmpeg/content/index.html)   [一个知名博客](https://blog.csdn.net/leixiaohua1020)  [一个中文论坛](http://bbs.chinaffmpeg.com/forum.php) 

直播：音视频会议、教育直播、游戏类直播

短视频：抖音 微视 火山小视频

网络视频：爱奇艺 优酷 腾讯

音视频通话：微信 QQ facebook

视频监控：幼儿园 保安

人工智能：人脸识别 智能家居

**网易云视频架构**

解复用-->音频解码|视频解码-->音频播放|视频渲染

视频渲染 yuv-->SurfaceView-->AVNativeWindow

## FFmpeg命令

下载windows及Linux版本FFmpeg，熟悉命令使用windows下的FFmpeg。

查看bin目录下的可执行文件，添加到环境变量

**命令模块**

- 基本信息查询命令
- 裁剪与合并命令
- 录制命令
- 直播命令
- 处理原始数据命令
- 各种滤镜命令

### **常用命令**

#### 录屏命令

```shell
ffmpeg -f gdigrab -framerate 30 -offset_x 0 -offset_y 0 -video_size 1920x1080 -i desktop out.mpg
gdigrab:表明我们是通过gdi抓屏的方式（mac下是avfoundation）
-framerate 30: 表示录屏的帧率为30
-offset_x:左上偏移量x
-offset_y:左上偏移量y
-video_size:需要录制的宽度和高度
-i:录屏方式，输入路径和名称及格式mpg
desktop:告诉ffmpeg要录制的是屏幕而不是一个窗口(可以使用窗口的ID录制一个窗口)
ctrl+c
```

#### 分解复用命令

定义：将完整的视频文件进行拆分。将拆分的信息作为素材合成所需要的新视频

第一步 抽取音频流

```
ffmpeg -i input.mp4 -acodec copy -vn out.aac
acodec:指定音频编码器
copy:指明只拷贝不做编解码
vn:v代表视频 n代表no 也就是无视频的意思
```

第二步 抽取视频流

```
ffmpeg -i input.mp4 -vcode copy -an out.h264
vcodec:指定视频编码器 
copy:指明只拷贝不做编解码
an:a代表音频 n代表no 也就是无音频的意思
```

第三步 合成视频

```
ffmpeg -i out.h264 -i out.aac -vcodec copy -acodec copy out.mp4
```



格式转换

ffmpeg -i out.mp4 -vcodec copy -acodec copy out.flv

#### 处理原始数据

定义：获取未经过编码的画面和音频，画面信息（一般是yuv） 音频是(pcm)

rgb三个通道 yuv一个通道。特效在原始数据上编辑。

**提取yuv数据** 

```
ffmpeg -i input.mp4 -an -c:v rawvideo -pix_fmt yuv420p out.yuv
-c:v rawvideo 指定将视频转成原始数据
-pixel_format yuv420p 指定转换格式为yuv420p(yuv的一种)
未经过编码的数据需要用到ffplay播放（ffplay -s 608x368 out.yuv）
```

**提取PCM数据**

```
ffmpeg -i input.mp4 -vn -ar 44100 -ac 2 -f s16le out.pcm
参数说明：
-ar：指定音频采样率44100 即44.1KHz
-ac：指定音频声道channel 2为双声道
-f：数据存储格式 s:Signed有符号的 16每一个数值用16位表示 l:little e:end
播放
ffplay -ar 44100 -ac 2 -f s16le out.pcm
```



### ffmpeg滤镜

原视频-->编码数据包-->修改的数据帧-->编码数据包-->原视频

裁剪视频为例

裁剪滤镜 

```
ffmpeg -i input.mp4 -vf crop=in_w-200:in_h-200 -c:v libx264 -c:a copy crop.mp4
```

提取PCM数据

```
ffmpeg -i input.mp4 -vn -ar 44100 -ac 2 -f s16le out.pcm
```





```
ffmpeg.exe -i 长臂猿录音10点59分.aac -acodec libmp3lame out1.mp3
ffmpeg.exe -i final.mp3 -acodec libmp3lame out1.mp3
ffmpeg.exe -i "concat:out2.mp3|out1.mp3" -acodec copy final.mp3
ffmpeg -i 1.mp3 -i 2.mp3 -filter_complex '[0:0] [1:0] concat=n=2:v=0:a=1 [a]' -map [a] out3.mp3
```



