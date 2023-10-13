 cmake_minimum_required(VERSION 3.4.1)
 set(FFMPEG_DIR ${CMAKE_SOURCE_DIR}/ffmpeg)
 set(RTMP_DIR ${CMAKE_SOURCE_DIR}/rtmp)
 include_directories(${FFMPEG_DIR}/include)
 set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L ${FFMPEG_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}")
 set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L ${RTMP_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}")
 file(GLOB src_files *.cpp *.h)
 add_library(
             gysoplayer
             SHARED
             ${src_files})

 target_link_libraries(
             gysoplayer
             avfilter avformat avcodec avutil swresample swscale
             log
             z
             OpenSLES
             rtmp
             android)