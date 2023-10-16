

## 视频解码及安全队列

[项目地址](https://gitee.com/guaishoun/ffmpegStudy1)

#### 编译集成librtmp

https://www.jianshu.com/p/212c61cac89c里面由内容

```
ffmpeg集成rtmp
cd he/ffmpeg/librtmp/
ls install/lib/
#修改rtmp版本
----#inlclude<librtmp/rtmp.c>
cd he/ffmpeg/librtmp/install/include/


#### ffmpeg 集成rtmp的源码在libavformat下
编译ffmpeg 时要开启--enable-librtmp 
```



```
1 对于ffmpeg：项目中更新打开rtmp的库及头文件重新覆盖
2 添加rmpt库
3 cmake中:rtmp库追加到CMAKE_CXX_FLAGS
4 target_link_libraries( ... rtmp)
5 app 权限要添加互联网权限
```

```
下载一个EV录屏
```

```
### 项目java中
player.setDataSource("rtmp://59.111.9.142/myapp/");
```

```
### 在浏览器中输入 
59.111.90.142:8080/stat
```



#### 写代码

```
### player Java中
添加onError(int errorCode)
getmethodID时 "(I)V"//括号里的表示参数 括号外面表示返回
```

### 手写安全队列

```
### 非0则true 非空即true
1 添加错误码Java层和native层
2 添加surfaceview
3 实现startNative方法
4 native-lib.cpp把player转为全局方法
5 同步锁
	pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
	pthread_mutex_lock(&mutex);
	pthread_mutex_unlock(&mutex);

6 native 处理error回调

```



#### 附件 相关学习网站

班级服务器 ***\*librtmp\**** 路径：`/root/he/ffmpeg/librtmp`



***\*RTMPDump：\****http://rtmpdump.mplayerhq.hu/



***\*音视频基础知识：\****https://www.jianshu.com/p/a2c09daee428



***\*JNI 基础：\****https://www.jianshu.com/p/e3bcff7e3b24



***\*Android Native视频绘制\****： https://www.jianshu.com/p/75f018f11eb9

