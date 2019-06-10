package ru.va.progrixivan.visionassistsant;

public abstract class Locate {


    public abstract String getAgreement();
    public abstract String getLocateSign();
    public abstract String getObject(int direction, int count, int id);
    public abstract String getFace(int direction, int gender, int age, int emotion);
    public abstract String getBus(BusSounding.Bus bus);
    public abstract String getSc(int sc);
    public abstract String getWait();
    public abstract String getSetting(int setting);

    public abstract String getValue(int currentSetting, int currentValue);

    public abstract String getSetAgr();

    public abstract String getGuideMsg(int guideMsg);
}
