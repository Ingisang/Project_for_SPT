package com.example.lee.video;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;
public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private MediaController mediaController;
    private static final String VIDEO_PATH = "http://172.30.88.35:81/1.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath(); // 기본적인 절대경로 얻어오기



        videoView = (VideoView)findViewById(R.id.video_view);
        videoView.setVideoPath(path+"/DCIM/Camera/20170906_174947.mp4");
        mediaController = new MediaController(MainActivity.this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
}