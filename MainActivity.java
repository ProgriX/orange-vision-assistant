package ru.va.progrixivan.visionassistsant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {




    private static final int REQUEST_CODE = 0;
    //ванино
    CameraBridgeViewBase cameraBridgeViewBase;


    private static int perse = 0;

    public static Vibrator vibrator;
    private static boolean isStarted = false;


    //int [] snowX = {10, 230, 80, 150, 143, 223, 12, 259, 283, 54}, snowY = {212, 65, 223, 145, 65, 134, 292, 273, 243, 123};



    static int sc = 1, scf = 0,
            maxSc = 3, maxScf[] = {0, 0, 5, 0};






    BaseLoaderCallback baseLoaderCallback;
    public static int SC_fun = -1, timer = 0, Window = 0;
    public static final int MAIN_WINDOW = 0, SETTINGS_WINDOW = 1, WORKING_WINDOW = 2;

    private static Mat SavedFrame;

    public static void pause(int mSec){
        try {
            Thread.sleep(mSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cameraPrepare(){
        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.myCameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                switch(status){
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }


            }
        };

        findViewById(R.id.bActivate).setOnTouchListener(new ScButton(ScButton.START_BUTTON));
        findViewById(R.id.bNext).setOnTouchListener(new ScButton(ScButton.NEXT_BUTTON));
        findViewById(R.id.bBack).setOnTouchListener(new ScButton(ScButton.BACK_BUTTON));


    }

    private void getPermissions(){

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (!(permissionStatus == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUEST_CODE);
            while (!(permissionStatus == PackageManager.PERMISSION_GRANTED)){
                pause(100);
                permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        pause(1000);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //if (!(permissionStatus == PackageManager.PERMISSION_GRANTED)) {
           // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                   // REQUEST_CODE_PERMISSION_FILE);
       // }
        if(!isStarted) {
            Sounding.AssetLink = this;
            Sounding.Agr_fun();
            Downloading.checkFiles();

            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            Sounding.LoadSoundSystem();

        }

        vibrator.vibrate(150);

        getPermissions();
        cameraPrepare();

    }

    @Override
    public void onCameraViewStopped() {
        //frame.release();
        //mat2.release();
        //mat3.release();
        //mat4.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        final Mat frame = inputFrame.rgba();

        Downloading.getStateLoading();


        if(SC_fun != -1 && !ScSystem.IsNetWorking) {
            new Thread() {
                public void run() {
                    switch (SC_fun) {

                        case 0:

                            SpeechGenerator.playGuide(SpeechGenerator.NOT_LOADED_ERROR);
                            SC_fun = -1;
                            break;

                        case 1:
                            SavedFrame = ScSystem.FD_fun(frame.clone());
                            break;

                        case 2:
                            SavedFrame = ScSystem.QOD_fun(frame);
                            break;

                        case 3:
                            SavedFrame = ScSystem.Bus_fun(frame.clone());
                            break;
                    }
                }
            }.start();
            }
        else
            pause(100);

        Mat smallFrame = new Mat();
        Imgproc.resize(frame,smallFrame,new Size(frame.cols() / 2 - 15, frame.rows() / 2 - 20));
        smallFrame.copyTo(frame.rowRange(0, frame.rows() / 2 - 20).colRange(0, frame.cols() / 2 - 15 ));


        return frame;


    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        if(!isStarted) {
            File file = new File(Environment.getExternalStorageDirectory() + "/dnns/config");
            Log.d("FilePath", "" + file.getAbsolutePath());
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File file1 = new File(Environment.getExternalStorageDirectory() + "dnns/config1");

            if(file1.exists()){
                try {
                    file1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There's a proble, bruh!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }

    }

    @Override
    protected void onDestroy(){

        SpeechGenerator.clearSounding();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
        super.onDestroy();


    }

}
