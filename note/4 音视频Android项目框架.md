[项目地址](https://gitee.com/guaishoun/ffmpegStudy1)

shell 脚本 -D定义宏 -U删除宏

可以通过ffbuild->config.log查看详细编译流程日志

库引入要保持正确链接顺序，运行编译脚本最开始会打印编译版本的配置开关

c++ new 出来的东西要delete

```C++ 
player{(videochannel ,audio channel )-> basechannel}
//路径赋值不能直接player赋值this->data_source = data_source；被释放了
this->datasource = new char[strlen(data_source)+1];//strlen不包括\0
strcopy(this->datasource,datasource);
//折构函数 对应delete
if(data_source){
    delete datasource
    dataasource =0 
}
prepare(){
    //解封装 ffmpeg 来解析data_source
    //可以直接解析吗？因为多媒体流有两种情况
    //1 文件 io流
    // 2 直播:网络
    //都不适合在主线程操作
    // Threadcreate里的void*(* __start_routine)(void *)参数 相当于Java run
    pthread_create(&pid,0,task_prepare,this);   
}
```



```C++
//线程函数 相当于run
void *task_prepare(void *args){
    .....
    NEPlayer * player = static_cast<NePlayer *>(agrs)
    player->_prepared();  
    return 0;//一定要记得
}
```



```
void _prepared(){
	1 处理输入
	释放字典
	int ret = openfile
	//open fail jni tell java
	if(ret){
		//jni 反射回调Java
		return;
	}
	
	2 查找流信息
	//fail jni tell java
	if(ret){
		//jni 反射回调Java
		return;
	}
	
	...
	//准备完成后 可以通知Java可以播放
	callback();
}
```

```C++
//初始化虚拟机
jint JNI_onload(javaVM * vm,void *agrs){//jni 最开始执行的地方
	javaVM = vm
	return
}

//获取env thiz javaVm 
void callback(){

	###this->instance = instance//不行
	//jobeject 一旦涉及跨方法跨线程需要创建全局引用
	this.instance = env->NewGlobalRef(instance)
	//获取jMethodId
	jclass clazz = env->GetObjectClass(instance)
	jmd_prepared = env->GetMethodId(clazz,"onprespared","()v")
	
	//调用,要切换到主线程
	//env不能跨线程
	if(thread_mode == THREAD_MAIN){
		env->CallVoidMethod(instanc,jmdMethodId)
	}else{
		//子线程 JNIEnv
		JNIEnv *env_child;
		javaVM->AttachCurrentThread(&env_child,0);
		env_child->CallVoidMethod(instance,jmd_prepared);
		javaVM->DetachCurrentThread();		
	}

	
}

//对应释放
env->DeleteGlobalRef(instance)
env =0 ;
instance=0;
jvm=0;


```

```C++
//实际编写过程
AVDictionary 不能随便用，会闪退的
子线程中一定要记得return 0;
```



