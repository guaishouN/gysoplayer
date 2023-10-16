### OpenSL ES 播放音频及音视频同步

[项目地址](https://gitee.com/guaishoun/ffmpegStudy1)

#### 音频解码入门学习

google native-audio示例：https://github.com/android/ndk-samples/blob/master/native-audio/app/src/main/cpp/native-audio-jni.c

OpenSL ES 播放音频：https://www.jianshu.com/p/f2a4653d51e5

Android 上播放PCM ：

AudioTrack

OpenSL ES 

src   > dst

48000 > 44100



#### 音视频同步

视频同步到音频的基本方法是：**如果视频超前音频，则不进行播放，以等待音频；如果视频落后音频，则丢弃当前帧直接播放下一帧，以追赶音频。**