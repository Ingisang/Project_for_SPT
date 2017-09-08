package pus.spt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
public class VideoRecorderMain extends Activity implements SurfaceHolder.Callback {

    //비디오뷰
    private VideoView videoView;
    private LinearLayout videoLayout;
    private MediaController mediaController;
    private static final String VIDEO_PATH = "http://172.30.88.35:81/1.mp4";
    ////////////////////////////////
    MediaRecorder mediaRecorder=null;

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera cam;
    static final int MY_PERMISSIONS_REQUEST =1;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/spt";
    File file =new File(path);
    String saveFile = null;
    Button startbtn, endbtn;
    Context context;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        if(!file.exists())file.mkdirs();
        path += "/love.mp4";
        versioncheack();
        setting();
///////////////////////////비디오뷰
        String devicepath = Environment.getExternalStorageDirectory().getAbsolutePath(); // 기본적인 절대경로 얻어오기
        videoLayout=(LinearLayout)findViewById(R.id.video_layout);
        videoView = (VideoView)findViewById(R.id.video_view);
        videoView.setVideoPath(devicepath+"/DCIM/Camera/20170906_174947.mp4");
        videoLayout.setAlpha((float)0.5);
        videoView.start();
        //mediaController = new MediaController(VideoRecorderMain.this);
        //mediaController.setAnchorView(videoView);
        //videoView.setMediaController(mediaController);

/////////////////////////////
        startbtn = (Button)findViewById(R.id.start_btn);
        endbtn = (Button) findViewById(R.id.end_btn);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mediaRecorder = new MediaRecorder();
                            cam.unlock();
                            mediaRecorder.setCamera(cam);
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                            mediaRecorder.setOrientationHint(90);
                            mediaRecorder.setOutputFile(path);
                            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                            mediaRecorder.prepare();

                            videoView.start();

                            Toast.makeText(VideoRecorderMain.this, "녹화를 시작합니다", Toast.LENGTH_SHORT).show();
                            mediaRecorder.start();
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                            mediaRecorder.release();
                            return;

                            // Log.i("---","Exception in thread");
                        }
                    }
                });
            }
        });
        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VideoRecorderMain.this, "녹화파일이 저장되었습니다", Toast.LENGTH_SHORT).show();
                mediaRecorder.stop();
                mediaRecorder.release();
                cam.lock();
                mediaRecorder = null;

                videoView.pause();

                Intent intent = new Intent(VideoRecorderMain.this,UploadActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
    private void setting(){
        cam = Camera.open();
        cam.setDisplayOrientation(90);
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (cam == null) {
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
        } catch (IOException e) {
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        refreshCamera(cam);
    }

    public void refreshCamera(Camera camera) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            cam.stopPreview();
        } catch (Exception e) {

        }
        setCamera(camera);
        try {
            cam.setPreviewDisplay(surfaceHolder);
            cam.startPreview();
        } catch (IOException ie) {

        }
    }


    public void setCamera(Camera camera) {
        //method to set a camera instance
        cam = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();

    }

    public void versioncheack(){
        if( ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(context, Manifest.permission_group.STORAGE)!=PackageManager.PERMISSION_GRANTED){
            //권한 없음
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST);
        }else{
            //권한 있음
        }

    }
    //권한 체크관련
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED &&grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3] ==PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다

                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    Toast.makeText(context,"권한 없이 해당 기능을 사용할 수 없습니다.",Toast.LENGTH_SHORT).show();
                    Intent inte = new Intent(VideoRecorderMain.this,VideoRecorderMain.class);
                    startActivity(inte);
                    finish();
                }
                return;
        }
    }

}
