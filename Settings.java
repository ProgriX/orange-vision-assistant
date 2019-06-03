package ru.va.progrixivan.visionassistsant;

public class Settings {


    private static final int settingsCount = 2;
    private static final int[] valueCount = {4, 2};
    private static int currentValue;
    private static  int currentSetting;

    private static char[] settings = new char[settingsCount];

    public static void SettingsLoad(){

    }

    public static void SettingsSave(){

    }

    public static void SoundSetting(){
        SpeechGenerator.playValue(currentSetting, currentValue);
    }

    public static void SetSetting(){

        settings[currentSetting] = (char)currentValue;


    }

    public static void SettingsNext(){

        currentSetting++;
        if(currentSetting >= settingsCount)
            currentSetting = 0;
        SpeechGenerator.playSetting(currentSetting);

    }

    public static void ValueNext(){

        currentValue++;
        if(currentValue >= valueCount[currentSetting]){
            currentValue = 0;
        }
        SpeechGenerator.playValue(currentSetting, currentValue);

    }

    public static void SettingsBack(){

        currentSetting--;
        if(currentSetting < 0)
            currentSetting = settingsCount;
        SpeechGenerator.playSetting(currentSetting);
    }

    public static void ValueBack(){

        currentValue--;
        if(currentValue < 0)
            currentValue = valueCount[currentSetting];
        SpeechGenerator.playValue(currentSetting, currentValue);

    }
}
