package ru.va.progrixivan.visionassistsant;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Locale;

import static ru.va.progrixivan.visionassistsant.MainActivity.SC_fun;
import static ru.va.progrixivan.visionassistsant.MainActivity.scf;
import static ru.va.progrixivan.visionassistsant.SpeechGenerator.generateSpeech;


public class Sounding {



    public static final String RAW = "raw", OBJECT = "w", COUNT = "c", MEN = "h", EMOTION = "e";






    final static int CENTER = 0, LEFT = 1, RIGHT = 2, INTERFACE_CANCEL = 4;

    final static String US = "e", RU = "";
    static String lng = RU;

    public static Context AssetLink;







    public static boolean IsEmptyThread = true;
    private static boolean WaitWorking = false;

    private static byte ScStreamCount = 0;
    private static int countCurrent;


    private static MediaPlayer mediaPlayerForWait;
    private static MediaPlayer mediaPlayerForStep;







    public static void LoadSoundSystem(){


        mediaPlayerForWait = MediaPlayer.create(AssetLink, R.raw.waittrack);
        mediaPlayerForStep = MediaPlayer.create(AssetLink, R.raw.jump);

    }





    public static MediaPlayer getSound(String type, int id){
        return MediaPlayer.create(AssetLink, AssetLink.getResources().getIdentifier(type + id, "raw", AssetLink.getPackageName()));
    }



    //recording
    // ***********************************************************************
    private static class Obj {


        // AOD & QOD sounding


        public int le = 0, ri = 0, ce = 0;

        public Obj (int l, int r, int c){
            le = l;
            ri = r;
            ce = c;
        }
    }
    private static Obj[] o = new Obj[80];

    public static void AddItem(int id, int x){
        if(x < 25)
            o[id].le++;
        else if(x > 75)
            o[id].ri++;
        else
            o[id].ce++;
    }


    //  start function



    public static void PlayObjAssets() {
        if(ScSystem.IsNetPrepared || MainActivity.SC_fun == 0) {

            ScSystem.IsNetPrepared = false;

            String objStream = "";

            for (int i = 0; i < 80; i++) {
                if (sc2f(i) || scf != 2) {

                    if (o[i].ce > 0)
                        objStream = objStream + SpeechGenerator.getObject(CENTER, o[i].ce, i) + ", ";

                    if (o[i].le > 0)
                        objStream = objStream + SpeechGenerator.getObject(LEFT, o[i].le, i) + ", ";

                    if (o[i].ri > 0)
                        objStream = objStream + SpeechGenerator.getObject(RIGHT, o[i].ri, i) + ", ";

                }
            }

            SpeechGenerator.generateSpeech(objStream);
            if(SC_fun == 2) mediaPlayerForStep.start();
            init();


            if(MainActivity.SC_fun == 2)PlayObjAssets();

        }
    }






    // ***********************************************************************
    // FD sounding

    //variables

    //functions





    public static void playFace(final int direction, final int gender, final int age, final int emo) {


        generateSpeech(SpeechGenerator.getFace(direction, gender, age, emo));

    }








    public static void PlaySetting(){
        new Thread() {
            @Override
            public void run() {



            }
        }.start();
    }





    public static void init() {
        for(int i = 0; i < 80; i++){
            o[i] = new Obj(0,0,0);
        }
        ScSystem.IsNetPrepared = false;

    }

    private static void chLng(){
        if(lng == RU){
            lng = US;
        }else{
            lng = RU;
        }

    }

    private static boolean sc2f(int i){
        boolean b = false;

        b = b || (scf == 0);
        b = b || ((scf == 1) && (i <= 12));
        b = b || (scf == 2) && (i > 12) && (i <= 23);
        b = b || (scf == 3) && ((i > 23) && (i <= 38) || (i >= 62));
        b = b || (scf == 4) && (i > 38) && (i <= 55);
        b = b || (scf == 4) && (i > 55) && (i <= 61);
        return b;
    }

    public static void WaitWorking(){
        WaitWorking = !WaitWorking;
        if(WaitWorking)
            mediaPlayerForWait.start();
        else {
            mediaPlayerForWait.pause();
            mediaPlayerForWait.seekTo(0);
        }
    }




    public static void Agr_fun() {

        if(Locale.getDefault().getLanguage() == "ru") {

            SpeechGenerator.setLocate(new RUS());

        } else {

            SpeechGenerator.setLocate(new ENG());

        }

    }



}
