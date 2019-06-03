package ru.va.progrixivan.visionassistsant;

import org.opencv.core.Rect;

public class ENG extends Locate {

    private final static String ENG = "en";

    private final static String agreement = "welcome to orange vision assistant!";


    private final static String[][] objects = {{"a bicycle", "a motorbike", "an airplane", "a bus", "a train", "a truck", "a boat", "a traffic light", "a fire hydrant", "a stop sign", "a parking meter", "a person", "a car", "a bench", "a bird", "a cat", "a dog", "a horse", "a sheep", "a cow", "an elephant", "a bear", "a zebra", "a giraffe", "a backpack", "an umbrella", "a handbag", "a tie", "a suitcase", "a frisbee", "skis", "a snowboard", "a sports ball", "a kite", "a baseball bat", "a baseball glove", "a skateboard", "a surfboard", "a tennis racket", "a bottle", "a wine glass", "a cup", "a fork", "a knife", "a spoon", "a bowl", "a banana", "an apple", "a sandwich", "an orange", "broccoli", "a carrot", "a hot dog", "a pizza", "a doughnut", "a cake", "a chair", "a sofa", "a potted plant", "a bed", "a dining table", "a toilet", "a TV monitor", "a laptop", "a computer mouse", "a remote control", "a keyboard", "a cell phone", "a microwave", "an oven", "a toaster", "a sink", "a refrigerator", "a book", "a clock", "a vase", "a pair of scissors", "a teddy bear", "a hair drier", "a toothbrush"},
            {"people", "bicycles", "cars", "motorbikes", "airplanes", "buses", "trains", "trucks", "boats", "traffic lights", "fire hydrants", "stop signs", "parking meters", "benches", "birds", "cats", "dogs", "horses", "sheep", "cows", "elephants", "bears", "zebras", "giraffes", "backpacks", "umbrellas", "handbags", "ties", "suitcases", "frisbees", "skis", "snowboards", "sports balls", "kites", "baseball bats", "baseball gloves", "skateboards", "surfboards", "tennis rackets", "bottles", "wine glasss", "cups", "forks", "knifes", "spoons", "bowls", "bananas", "apples", "sandwiches", "oranges", "broccoli", "carrots", "hot dogs", "pizzas", "donuts", "cakes", "chairs", "sofas", "potted plants", "beds", "dining tables", "toilets", "TV monitors", "laptops", "computer mouses", "remotes", "keyboards", "cell phones", "microwaves", "ovens", "toasters", "sinks", "refrigerators", "books", "clocks", "vases", "scissors", "teddy bears", "hair driers", "tooth brushes"}};
    private final static String[] emotionsStrings = {"is an angry", " is a disgusted", "is a scared", "is a happy", "is a sad", "is a surprised", "is a calm"};

    private final static String[][] manStrings = {
            {"girl", "woman", "lady", "old woman"},
            {"boy", "guy", "man", "old man"}
    };

    private final static String[] scString = {
            "accurate object detection", "face detection", "quick object detection", "bus detection"
    };


    private final static int CENTER = 0, LEFT = 1, RIGHT = 2;
    private final static String[] directions = {"in front of you", "on the left", "on the right"};


    private static String getDirection(int direction) {

        return directions[direction];

    }

    @Override
    public String getAgreement() {
        return agreement;
    }

    @Override
    public String getLocateSign() {
        return ENG;
    }

    private static String getCount(int count) {

        if(count > 1){

            return "are" + count;

        }

        return "is ";

    }

    private static int getCountCase (int count){

        if(count > 1){

            return 1;

        }

        return 0;

    }

    private static String getObject(int id, int count){

        return objects[getCountCase(count)][id];

    }

    private static String getDirectionByX(int direction) {

        if(direction > 75)
            return directions[RIGHT];

        if(direction < 25)
            return directions[LEFT];

        return directions[CENTER];

    }

    private static String getEmotion(int emo) {

        return emotionsStrings[emo];

    }

    private static String getMan(int age, int gender) {

        return manStrings[gender][age];

    }

    @Override
    public String getFace(int direction, int gender, int age, int emotion) {
        return getDirectionByX(direction) +
                " " + getEmotion(emotion) +
                " " + getMan(age, gender);
    }


    @Override
    public String getObject(int direction, int count, int id) {

        return getDirection(direction) + " " +
                getCount(count) + " " +
                getObject(id, count);

    }

    public static String getNum(String str){

        if(str.length() > 0){
            str = "number " + str;
        }

        return str;
    }

    private String getDoorPlace(int x, Rect rect) {

        x = (x - rect.x) * 100 / rect.width;

        if(x > 75){

            return "on the right side";

        }

        if (x < 25) {

            return "on the left side";

        }

        return "in the center";

    }

    private String getDoors(BusSounding.Bus bus) {

        String str = "";

        for(int i = 0; i < bus.doorsClose.size(); i ++){

            str += getDoorPlace(bus.doorsClose.get(i), bus.rect) + " are closed doors, ";

        }

        for(int i = 0; i < bus.doorsOpen.size(); i ++){

            str += getDoorPlace(bus.doorsOpen.get(i), bus.rect) + " are opened doors, ";

        }

        return str;

    }

    @Override
    public String getBus(BusSounding.Bus bus) {
        return getDirectionByX(bus.rect.x + bus.rect.width / 2) + " is the bus " +
                getNum(bus.numberWay) + ". " +
                getDoors(bus);
    }

    @Override
    public String getSc(int sc) {
        return scString[sc];
    }

    @Override
    public String getWait() {
        return null;
    }

    @Override
    public String getSetting(int setting) {
        return null;
    }

    @Override
    public String getValue(int currentSetting, int currentValue) {
        return null;
    }

    @Override
    public String getSetAgr() {
        return null;
    }
}
