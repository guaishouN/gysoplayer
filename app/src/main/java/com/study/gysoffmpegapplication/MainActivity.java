package com.study.gysoffmpegapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GySoPlayer gySoPlayer;
    private SurfaceView surfaceView;
    private AppCompatSeekBar seekBar;//进度条
    private TextView tvTime;//视频时间
    private boolean isTouch;//正在拖拽进度条
    private boolean isSeek;//是否seek
    private int mDuration = 0;//视频长度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceview);
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(this);
        tvTime = findViewById(R.id.time_tv);
        int permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            },89);
        }else{
            prepareVedio();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED){
            prepareVedio();
        }else{
            Toast.makeText(this,"没有权限不能播放",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 准备video
     */
    private void prepareVedio() {
        String filePath = Environment.getExternalStorageDirectory()+ File.separator + "demo.mp4";
        File file = new File(filePath);
        if (!file.exists()){
            Log.e(TAG, "playVideo: file not exist");
            return;
        }
        gySoPlayer = new GySoPlayer(file.getAbsolutePath());
        //gySoPlayer = new GySoPlayer("rtmp://59.111.90.142/myapp/");
        gySoPlayer.setSurfaceView(surfaceView);
        gySoPlayer.prepare();
        gySoPlayer.setOnStatCallback(new GySoPlayer.OnStatCallback() {
            @Override
            public void onPrepared() {
                mDuration = gySoPlayer.getDuration();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "准备播放完毕", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(int errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "播放视频出错了!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgress(int currentPlayTime){
                //底层返回进度更新
                if(!isTouch){
                    runOnUiThread(()->{
                        if(mDuration!=0){
                            if(isSeek) {
                                isSeek = false;
                                return;
                            }
                            tvTime.setText(getMinutes(currentPlayTime) + ":" + getSeconds(
                                    currentPlayTime) + "/" + getMinutes(mDuration) + ":" + getSeconds(
                                    mDuration));
                            seekBar.setProgress(currentPlayTime * 100 / mDuration);
                        }
                    });
                }
            }
        });
    }

    /**
     * 点击播放
     * @param view view
     */
    public void playVideo(View view) {
        gySoPlayer.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            //用户主动设置进度
            tvTime.setText(getMinutes(progress * mDuration / 100) + ":" + getSeconds(
                    progress * mDuration / 100) + "/" + getMinutes(mDuration) + ":" + getSeconds(
                    mDuration));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //松手
        isTouch = false;
        isSeek = false;
        //当前进度
        int seekBarProcess = seekBar.getProgress();
        //转换为底层播放时间
        int playProgress = seekBarProcess*mDuration /100;
        //设置到底层
        gySoPlayer.seek(playProgress);
    }

    private String getMinutes(int duration) {
        int minutes = duration / 60;
        if (minutes <= 9) {
            return "0" + minutes;
        }
        return "" + minutes;
    }

    private String getSeconds(int duration) {
        int seconds = duration % 60;
        if (seconds <= 9) {
            return "0" + seconds;
        }
        return "" + seconds;
    }
}
