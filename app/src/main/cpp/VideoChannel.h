//
// Created by GuaishouN on 2020/4/25.
//

#ifndef GYSOFFMPEGAPPLICATION_VIDEOCHANNEL_H
#define GYSOFFMPEGAPPLICATION_VIDEOCHANNEL_H

extern "C"{

};

#include "BaseChannel.h"
#include "AudioChannel.h"

typedef void (*RenderCallback)(uint8_t *,int,int,int);
class VideoChannel : public BaseChannel{
    public:
         VideoChannel(int stream_index,AVCodecContext *pContext,AVRational time_base, int fps);
         ~VideoChannel();
         void start();
         void stop();
         void video_decode();
         void video_play();
         void setRenderCallback(RenderCallback renderCallback);

    void setAudioChannel(AudioChannel *pChannel);

private:
    pthread_t pid_video_decode;
    pthread_t pid_video_play;
    RenderCallback renderCallback;
    AudioChannel *audioChannel;
    int fps;
};


#endif //GYSOFFMPEGAPPLICATION_VIDEOCHANNEL_H
