package ru.va.progrixivan.visionassistsant;

import android.view.MotionEvent;
import android.view.View;

import static ru.va.progrixivan.visionassistsant.MainActivity.SC_fun;
import static ru.va.progrixivan.visionassistsant.MainActivity.sc;
import static ru.va.progrixivan.visionassistsant.SpeechGenerator.SoundInterface;

public class ScButton implements View.OnTouchListener {

    private long startTime;
    private int Button;
    public static final int START_BUTTON = 0, NEXT_BUTTON = 1, BACK_BUTTON = 2;

    public ScButton(int button){
        Button = button;
    }

    private static void onStartButtonClick(){
        SoundInterface(sc);
    }

    private static void onNextButtonClick(){
        sc++;
        if(sc > MainActivity.maxSc)sc=0;
        SoundInterface(sc);
    }

    private static void onBackButtonClick(){
        sc--;
        if(sc < 0)sc=MainActivity.maxSc;
        SoundInterface(sc);
    }

    private static void onStartButtonPress(){



        switch (sc) {
            case 0:
                SC_fun = 0;
                break;
            case 1:
                SC_fun = 1;
                break;
            case 2:
                SC_fun = SC_fun == -1 ? 2 : -1;
                break;

            case 3 :
                SC_fun = SC_fun == -1 ? 3 : -1;
            break;
        }
    }

    private static void onNextButtonPress(){

    }

    private static void onBackButtonPress(){

        MainActivity.Window = MainActivity.SETTINGS_WINDOW;
        SpeechGenerator.playSetAgr();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                startTime = System.currentTimeMillis();
                MainActivity.vibrator.vibrate(50);
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                long totalTime = System.currentTimeMillis() - startTime;
                long totalSecunds = totalTime / 100;
                if (totalSecunds >= 5) {
                    MainActivity.vibrator.vibrate(100);
                    switch (MainActivity.Window) {
                        case MainActivity.MAIN_WINDOW:
                            switch (Button) {
                                case START_BUTTON:
                                    onStartButtonPress();
                                    break;
                                case NEXT_BUTTON:
                                    onNextButtonPress();
                                    break;
                                case BACK_BUTTON:
                                    onBackButtonPress();
                                    break;
                            }
                            break;

                        case MainActivity.WORKING_WINDOW:
                            break;

                        case MainActivity.SETTINGS_WINDOW:
                            switch (Button) {
                                case START_BUTTON:
                                    Settings.SetSetting();
                                    break;
                                case NEXT_BUTTON:
                                    Settings.SettingsNext();
                                    break;
                                case BACK_BUTTON:
                                    Settings.SettingsBack();
                                    break;
                            }
                            break;

                    }



                } else {
                    switch (MainActivity.Window) {
                        case MainActivity.MAIN_WINDOW:
                            switch (Button) {
                                case START_BUTTON:
                                    onStartButtonClick();
                                    break;
                                case NEXT_BUTTON:
                                    onNextButtonClick();
                                    break;
                                case BACK_BUTTON:
                                    onBackButtonClick();
                                    break;
                            }
                            break;
                        case MainActivity.WORKING_WINDOW:
                            break;


                        case MainActivity.SETTINGS_WINDOW:
                            switch (Button) {
                                case START_BUTTON:
                                    Settings.SoundSetting();
                                    break;
                                case NEXT_BUTTON:
                                    Settings.ValueNext();
                                    break;
                                case BACK_BUTTON:
                                    Settings.ValueBack();
                                    break;
                            }
                            break;
                    }

                }
                break;
        }
        return true;
    }
}
