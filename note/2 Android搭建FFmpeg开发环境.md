# Android搭建FFmpeg开发环境

### 简介

两个步骤 

1 下载ffmpeg源文件在linux下编译

2 在Android中配置

### 下载ffmpeg源文件在linux下编译

FFmpeg是一套可以用来记录、转换数字音视频，并能将其
转化为流的开源计算机程序。
FFmpeg是一个多媒体视频处理工具，有非常强大的功能包含视频采集功能、
视频格式转换、视频抓图、给视频加水印等功能
免费开源跨平台的多媒体框架。录制转换及流转化音视频的完整解决方案
ffmpeg
ffplay
ffprobe

ffmpeg开发库：
1 libavcodec 音视频编解码
2 libavutil 简化编程
3 libavformat 多媒体解复用器和解复用器
4 libavdevice 输入输出
5 libadfilter 过滤
6 libswscale 像素格式转换
7 libswresample音频格式转换

ffmpeg要在Linux下编译
在NDK环境下编译
ffmpeg编译步骤：
1 右键复制链接 注意不要高于17cd// 百度搜索ffmpeg ndk
2 wget https://dl.google.com/android......下载
3 uzip 解压
4 配置环境变量 vim/etc/profile
5 在这个文件的最下面添加NDKROOT=/home/guaishou/Documents/ndk/android-ndk-r17c //可以用pwd获取文件路径
6 追加到PATH, export PATH=\$NDKROOT:\$PATH 并保存
7 配置生效 source /etc/profile
8 ndk-build 测试配置
9 同样下载并解压ffmpeg: tar xvf ffmpeg-4.0.5.tar.bz2
10 进入ffmpeg解压后的目录 vim configure
11 ./configure --disable-x86asm //编译只能在linux上运行
12 ./configure --help //查看配置
13.参数太多，vim build.sh
NDk_root=ndkpath
...
./configure \
--prefix=$PREFIX \
..
make clean
make install



### Android中配置

ffmpeg 是用C编写的，头文件要用extern “C”包裹引入，

return env->NewStringUTF(av_version_info());

minsdk要大于15，njia是在21后加入的

##### gradle.build

```shell
    ....
    defaultConfig {
		....
        externalNativeBuild {
            cmake {
                cppFlags ""
                abiFilters "armeabi-v7a"
            }
        }

        ndk{
            abiFilters("armeabi-v7a")
        }
    }
    ....
```



##### CMakeLists.txt

```shell
cmake_minimum_required(VERSION 3.4.1)
include_directories(${CMAKE_SOURCE_DIR}/include)
#link_directories(${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")
add_library( native-lib
             SHARED
             native-lib.cpp )
target_link_libraries(
        native-lib
        -Wl,--start-group
        avcodec avfilter avformat avutil swresample swscale
        -Wl,--end-group
        log
        z
        android
)
```

##### native-lib.c

```c
#include <jni.h>
#include <string>

extern "C"{
#include <libavcodec/avcodec.h>
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_study_ffmpegenvbuildapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(av_version_info());
}
```

##### 工程目录结构

![工程目录截图](..\..\..\images\截图\ffmpeg工程目录结构.PNG)



#### FFmpeg在linux下构建脚本

```shell
#!/bin/bash
NDK_ROOT=/home/guaishou/Documents/ndk/android-ndk-r17c
#TOOLCHAIN 变量指向ndk中的交叉编译gcc所在的目录
TOOLCHAIN=$NDK_ROOT/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64/
#FLAGS与INCLUDES变量 可以从AS ndk工程的.externativeBuild/cmake/debug/armeabi-v7a/build.ninja中拷贝，需要注意的是**地址**
FLAGS="-isystem $NDK_ROOT/sysroot/usr/include/arm-linux-androideabi -D__ANDROID_API__=21 -g -DANDROID -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16 -mthumb -Wa,--noexecstack -Wformat -Werror=format-security -std=c++11  -O0 -fPIC"
INCLUDES="-isystem $NDK_ROOT/sources/cxx-stl/llvm-libc++/include -isystem $NDK_ROOT/sources/android/support/include -isystem $NDK_ROOT/sources/cxx-stl/llvm-libc++abi/include"

#执行configure脚本，用于生成makefile
#--prefix : 安装目录
#--enable-small : 优化大小
#--disable-programs : 不编译ffmpeg程序(命令行工具)，我们是需要获得静态(动态)库##。
#--disable-avdevice : 关闭avdevice模块，此模块在android中无用
#--disable-encoders : 关闭所有编码器 (播放不需要编码)
#--disable-muxers :  关闭所有复用器(封装器)，不需要生成mp4这样的文件，所以关闭
#--disable-filters :关闭视频滤镜
#--enable-cross-compile : 开启交叉编译（ffmpeg比较**跨平台**,并不是所有库都有这#么happy的选项 ）
#--cross-prefix: 看右边的值应该就知道是干嘛的，gcc的前缀 xxx/xxx/xxx-gcc 则给xx#x/xxx/xxx-
#disable-shared enable-static 不写也可以，默认就是这样的。
#--sysroot: 
#--extra-cflags: 会传给gcc的参数
#--arch --target-os :
PREFIX=./android/armeabi-v7a
./configure \
--prefix=$PREFIX \
--prefix=$PREFIX \
--enable-small \
--disable-programs \
--disable-avdevice \
--disable-encoders \
--disable-muxers \
--disable-filters \
--enable-cross-compile \
--cross-prefix=$TOOLCHAIN/bin/arm-linux-androideabi- \
--disable-shared \
--enable-static \
--sysroot=$NDK_ROOT/platforms/android-21/arch-arm \
--extra-cflags="$FLAGS $INCLUDES" \
--extra-cflags="-isysroot $NDK_ROOT/sysroot" \
--arch=arm \
--target-os=android 

make clean
make install
```



