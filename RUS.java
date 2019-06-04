package ru.va.progrixivan.visionassistsant;

import org.opencv.core.Rect;

public class RUS extends Locate {

    private final static String RU = "ru";

    private final static String agreement = "вас приветствует апельсин!";

    private final static String[] scString = {"точное определение", "определение лиц", "быстрое определение", "определение автобусов"};

    private final static String[][] objects = {
            {"человек", "велосипед", "машина", "мотоцикл", "самолёт", "автобус", "поезд", "грузовик", "лодка", "светофор", "пожарный кран", "знак стоп", "парковочный счётчик", "скамейка", "птица", "кошка", "собака", "лошадь", "овца", "корова", "слон", "медведь", "зебра", "жираф", "рюкзак", "зонт", "сумка", "галстук", "чемодан", "фризби", "лыжы", "сноуборд", "мяч", "бумажный змей", "бейзбольная бита", "бейзбольные перчатки", "скейиборд", "сёрфборт", "тэнисная ракетка", "бутылка", "бокал", "чашка", "вилка", "нож", "ложка", "тарелка", "банан", "яблоко", "сэндвич", "апельсин", "броколи", "морковка", "хотдок", "пицца", "пончик", "торт", "стул", "диван", "комнатное растение", "кровать", "стол", "туалет", "монитор", "ноутбук", "мышь", "пульт", "клавиатура", "мобильный телефон", "микроволновка", "печь", "тостер", "раковина", "холодильник", "книга", "часы", "ваза", "ножницы", "плюшевый мишка", "зубная щётка"},
            {"человека", "велосипеда", "машины", "мотоцикла", "самолёта", "автобуса", "поезда", "грузовика", "лодки", "светофора", "прожарных крана", "знака стоп", "парковочных счётчика", "скамейки", "птицы", "кошки", "собаки", "лошади", "овци", "коровы", "слона", "медведя", "зебры", "жирафа", "рюкзака", "зонта", "сумки", "галстука", "чемодана", "фризби", "лыжи", "сноуборда", "мяча", "бумажных змея", "бейзбольных биты", "бейзбольные перчатки", "скейтборда", "сёрфборта", "тэнисных ракетки", "бутылки", "бокала", "чашки", "вилки", "ножа", "ложки", "тарелки", "банана", "яблока", "сэндвича", "апельсина", "броколи", "морковки", "хотдога", "пиццы", "пончика", "торта", "стула", "дивана", "комнатных растения", "кровати", "стола", "туалета", "монитара", "ноутбука", "мыши", "пульта", "клавиатуры", "мобильных телефона", "микроволновки", "печи", "тостера", "раковины", "холодильника", "книги", "часы", "вазы", "пары ножниц", "плюшевых мишки", "зубные щётки"},
            {"человек", "велосипедов", "машин", "мотоциклов", "самолётов", "автобусов", "поездов", "грузовиков", "лодок", "светофоров", "пожарных кранов", "знаков стоп", "парковочных счётчиков", "скамеек", "птиц", "мошек", "собак", "лошадей", "овц", "коров", "слонов", "медведей", "зебр", "жирафов", "рюкзаков", "зонтов", "сумок", "галстуков", "чемоданов", "фризби", "лыжи", "сноубордов", "мячей", "бумажных змеев", "бейзбольных бит", "бейзбольные перчатки", "скейтбордов", "сёрфбортов", "тэнисных ракеткок", "бутылок", "бокалов", "чашек", "вилок", "ножей", "ложек", "тарелок", "бананов", "яблок", "сэндвичей", "апельсинов", "броколи", "морковок", "хотдогов", "пицц", "пончиков", "тортов", "стульев", "диванов", "комнатных растений", "кроватей", "столов", "туалетов", "мониторов", "ноутбуков", "мышей", "пультов", "клавиатур", "мобильных телефанов", "микроволновок", "печей", "тостеров", "раковин", "холодильников", "книг", "часы", "ваз", "пар ножниц", "плюшевых мишек", "зубных щеток"}
    };



    private final static String[][] emotionsStrings = {
            {"злая", "чувствующая отвращение", "испуганная", "счастливая", "грустная", "удивлённая", "спокойная"},
            {"злой", "чувствующий отвращение", "испуганный", "счастливый", "грустный", "удивлённый", "спокойный"}
    };

    private final static String[][] manStrings = {
            {"девочка", "девушка", "дама", "пожилая дама"},
            {"мальчик", "парень", "мужчина", "пожилой мужчина"}
    };

    private final static String[] settingsString = { "скорость", "язык" };
    private final static String[][] settingsValue = { { "скорость речи 1 икс" , "скорость речи 2 икс" , "скорость речи 3 икс" , "скорость речи 4 икс" } ,
            { "русский" , "английский" } };

    private final static int CENTER = 0, LEFT = 1, RIGHT = 2;
    private final static String[] directions = { "перед вами", "слева", "справа" };



    private static int getCountCase(int count){

        if( count % 100 > 10 && count % 100 < 20){

            return 2;

        }

        if(count % 10 == 1){

            return 0;

        }

        if(count % 10 == 0){

            return 2;

        }

        if (count % 10 < 5){

            return 1;

        }

        return 2;
    }

    private static String getCount(int count) {

        if(count > 1){

            return count + "";

        }

        return "";

    }

    private static String getObject(int id, int count){

        return objects[getCountCase(count)][id];

    }

    private static String getDirection(int direction) {

        return directions[direction];

    }

    @Override
    public String getObject(int direction, int count, int id) {

        return getDirection(direction) + " " +
                getCount(count) + " " +
                getObject(id, count);

    }



    private static String getMan(int age, int gender) {

        return manStrings[gender][age];

    }

    private static String getEmotion(int emo, int gender) {

        return emotionsStrings[gender][emo];

    }

    private static String getDirectionByX(int direction) {

        if(direction > 75)
            return directions[RIGHT];

        if(direction < 25)
            return directions[LEFT];

        return directions[CENTER];

    }

    @Override
    public String getFace(int direction, int gender, int age, int emotion) {
        return getDirectionByX(direction) +
                " " + getEmotion(emotion, gender) +
                " " + getMan(age, gender);
    }


    private String getDoorPlace(int x, Rect rect) {

        x = (x - rect.x) * 100 / rect.width;

        if(x > 75){

            return "справой стороны";

        }

        if (x < 25) {

            return "слевой стороны";

        }

        return "по центру";

    }

    private String getDoors(BusSounding.Bus bus) {

        String str = "";

        for(int i = 0; i < bus.doorsClose.size(); i ++){

            str += getDoorPlace(bus.doorsClose.get(i), bus.rect) + " автобуса закрытая дверь, ";

        }

        for(int i = 0; i < bus.doorsOpen.size(); i ++){

            str += getDoorPlace(bus.doorsOpen.get(i), bus.rect) + " автобуса открытая дверь, ";

        }

        return str;

    }



    public static String getNum(String str){

            if(str.length() > 0){
                str = "под номером " + str;
            }



        return str;
    }

    @Override
    public String getBus(BusSounding.Bus bus) {



        return getDirectionByX(bus.rect.x + bus.rect.width / 2) + " автобус" +
                getNum(bus.numberWay) + ". " +
                getDoors(bus);

    }

    @Override
    public String getSc(int sc) {
        return scString[sc];
    }

    @Override
    public String getWait() {
        return " дождитесь окончания загрузки";
    }

    @Override
    public String getSetting(int setting) {
        return settingsString[setting];
    }

    @Override
    public String getValue(int currentSetting, int currentValue) {
        return settingsValue[currentSetting][currentValue];
    }

    @Override
    public String getSetAgr() {
        return "настройки";
    }

    @Override
    public String getGuideMsg(int guideMsg) {

        String str = "";

        switch (guideMsg){

            case SpeechGenerator.NOT_LOADED_ERROR : str = "точное определение не установленно";
            break;

        }

        return str;
    }


    @Override
    public String getAgreement() {

        return agreement;

    }

    @Override
    public String getLocateSign() {
        return RU;
    }


}
