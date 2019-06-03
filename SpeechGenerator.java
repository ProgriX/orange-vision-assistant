package ru.va.progrixivan.visionassistsant;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeechGenerator {


    public static float speechSpeed = 3f;
    private static TextToSpeech speechGenerator;


    private static Locate locate;

    public static void setLocate(Locate newLocate) {

        locate = newLocate;

        speechGenerator = new TextToSpeech( Sounding.AssetLink , new TextToSpeech.OnInitListener () {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    speechGenerator.setLanguage ( new Locale(locate.getLocateSign()) );

                }
                speechGenerator.setSpeechRate(speechSpeed);

                if(Downloading.notLoaded) {

                    generateSpeech(getAgreement() + getWait());

                }else{

                    generateSpeech(getAgreement());
                }

            }
        } );

    }

    private static String getWait() {

        return locate.getWait();

    }

    public static String getAgreement(){

        return locate.getAgreement();

    }

    public static String getFace(int direction, int gender, int age, int emotion){

        return locate.getFace(direction, gender, age, emotion);

    }

    public static String getObject (int direction, int count, int id){

        return locate.getObject(direction, count, id);

    }

    public static void generateSpeech(String str){

        speechGenerator.speak(str, TextToSpeech.QUEUE_FLUSH , null);

    }

    public static void clearSounding() {

        if (speechGenerator != null) {
            speechGenerator.stop ();
            speechGenerator.shutdown ();
        }

    }

    public static String getBus(BusSounding.Bus bus){

        return locate.getBus(bus);

    }

    public static Boolean isPlaying(){

        return speechGenerator.isSpeaking();

    }

    public static void SoundInterface(final int sc){

        generateSpeech(locate.getSc(sc));

    }

    public static void playSetting(int currentSetting) {

        generateSpeech(locate.getSetting(currentSetting));

    }

    public static void playValue(int currentSetting, int currentValue) {

        generateSpeech(locate.getValue(currentSetting, currentValue));

    }

    public static void playSetAgr() {

        generateSpeech(locate.getSetAgr());

    }
}
