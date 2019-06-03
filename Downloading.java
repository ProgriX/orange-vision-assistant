package ru.va.progrixivan.visionassistsant;

import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;




public class Downloading extends Thread {

    private static int percent = 0;
    private static long totalSize = 0;
    private static int timer = 0;


    private final static String[] fileNames = { "BUSNUM.pb", "busyolo.cfg", "busyolo.weights",
            "deploy.prototxt", "facemodel.pb", "new_emotion_net.pb",
            "res10_300x300_ssd_iter_140000.caffemodel", "yolov3-tiny.cfg",
            "yolov3-tiny.weights", "yolov3.cfg", "yolov3.weights" };

    private final static long[] fileSize = { 93477633, 2028, 34732716,
            28092, 38569026, 5950014,
            10666211, 1915, 35434956,
            8342, 248007048  };

    private final static String[] filePath = {
            "https://drive.google.com/uc?export=download&id=10egK9kMxIUxjAQAkxnVrkQw_RF1bidck",
            "https://drive.google.com/uc?export=download&id=1notSQVi-QRv10IihK47GNKp_h_PqtJIi",
            "https://drive.google.com/uc?export=download&id=1XiWxu3y0Kja9rKnD-RkiZw-k9hFpLs7J",
            "https://drive.google.com/uc?export=download&id=1lwgNLVhKNnHq9rBa2ertta8XG0lQFQKT",
            "https://drive.google.com/uc?export=download&id=1cS0CRVYCeNoVUYRXlDAthvVp-FjMCBfX",
            "https://drive.google.com/uc?export=download&id=1P8JuUabGJd0J2sXONYRmTM6Vk3t-Soqe",
            "https://drive.google.com/uc?export=download&id=1udBe5EiWJDYj0195jGSuFmJqMJOCMHjA",
            "https://drive.google.com/uc?export=download&id=1Hpnqav2LZ3nddFKwWMG6TavGVq5ZAfsg",
            "https://drive.google.com/uc?export=download&id=1UqNe1O2BN0M8Nt0BpP2FQ6FecVzdHK4j",
            "https://drive.google.com/uc?export=download&id=1JfsszGs2vZ2FxrY8LLN0RwddSWZgUqdD",
            "https://pjreddie.com/media/files/yolov3.weights"
    };

    private static File loadFile;
    private static boolean[] exist;
    private static long loaded = 0;
    public static boolean notLoaded = false;

    public static void checkFiles(){

        exist = new boolean[fileNames.length];

        for(int i = 0; i < fileNames.length; i++){


            File file = new File(Sounding.AssetLink.getFilesDir() + "/" + fileNames[i]);

            if(!file.exists()){


                    Log.d("qwert", "" + i);
                    totalSize = totalSize + fileSize[i];
                    exist[i] = true;
                    notLoaded = true;


            }else{
                if(file.length() != fileSize[i]){

                    Log.d("qwert", "" + i);
                    totalSize = totalSize + fileSize[i];
                    exist[i] = true;
                    notLoaded = true;

                }
            }

        }

        if(notLoaded){

            new Downloading().start();


        }

    }

    public static void getStateLoading(){
        if(timer > 20) {
            if (loadFile != null) {
                int currentPercent = (int) ((loadFile.length() + loaded) * 100 / totalSize);

                if (currentPercent != percent) {

                    percent = currentPercent;
                    SpeechGenerator.generateSpeech(percent + "");

                }
            }
            timer = 0;
        } else {
            timer++;
        }

    }

    private static void onDownloadComplete(boolean success) {
        SpeechGenerator.generateSpeech( "успешно! для коректной работы перезапустите приложение");
        loadFile = null;
    }

    @Override
    public void run() {

        try {


            for(int i = 0; i < fileNames.length; i++){

                if(exist[i]){

                    String destFileName = fileNames[i];
                    String src = filePath[i];
                    loadFile = new File(Sounding.AssetLink.getFilesDir() + "/" + destFileName);

                    Log.d("qwert", Sounding.AssetLink.getFilesDir() + "/" + destFileName);

                    Log.d("qwert", "load: " + i);
                    FileUtils.copyURLToFile(new URL(src), loadFile);
                    loaded = loaded + loadFile.length();
                }

            }



            onDownloadComplete(true);


        } catch (IOException e) {
            e.printStackTrace();
            onDownloadComplete(false);

        }
    }
}