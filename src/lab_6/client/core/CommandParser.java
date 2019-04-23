package lab_6.client.core;

import lab_6.message.Message;
import lab_6.world.creation.Dancer;
import lab_6.world.state.DynamicsState;
import lab_6.world.state.FeelState;
import lab_6.world.state.PositionState;
import lab_6.world.state.ThinkState;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;

public class CommandParser {
    private static String userLogin;
    public static void setUserLogin(String login){
        userLogin = login;
    }

    public static Dancer getDancerFromJSONObject(JSONObject JSONobj){
        Dancer tempDancer = new Dancer("NoName");
        tempDancer.dynamicsStateState = DynamicsState.NEUTRAL;
        tempDancer.feelState = FeelState.NEUTRAL;
        tempDancer.thinkState = ThinkState.NEUTRAL;
        tempDancer.positionState = PositionState.NEUTRAL;
        for (Map.Entry<String, Object> iter : JSONobj.toMap().entrySet()){
            if (! tempDancer.setParam(iter.getKey(), iter.getValue().toString())){
                System.out.println(new StringBuffer()
                        .append("[ERROR]\n")
                        .append("Объект Dancer ")
                        .append(tempDancer)
                        .append(" не имеет поля ")
                        .append(iter.getKey())
                        .append(", принимающего значение ")
                        .append(iter.getValue().toString())
                        .append("\n\"Поле не будет создано\"\n")
                );
            }
        }
        return tempDancer;
    }
    public static Message getMessageFromJSON(String inputJSON){
        Message response = new Message();
        inputJSON = inputJSON.strip();
        if (inputJSON.length() > 5 && inputJSON.substring(0,6).equalsIgnoreCase("status")){
            response.text = "status";
            return response;
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("show")){
            response.text = "show";
            return response;
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("save")){
            response.text = "save";
            return response;
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("load")){
            response.text = "load";
            return response;
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("info")){
            response.text = "info";
            return response;
        }
        else if (inputJSON.length() > 9 && inputJSON.substring(0,10).equalsIgnoreCase("add_if_max")){
            response.text = "add_if_max";
            inputJSON = inputJSON.substring(10);
        }
        else if (inputJSON.length() > 9 && inputJSON.substring(0,10).equalsIgnoreCase("add_if_min")){
            response.text = "add_if_min";
            inputJSON = inputJSON.substring(10);
        }
        else if (inputJSON.length() > 5 && inputJSON.substring(0,6).equalsIgnoreCase("remove")){
            response.text = "remove";
            inputJSON = inputJSON.substring(6);
        }
        else if (inputJSON.length() > 2 && inputJSON.substring(0,3).equalsIgnoreCase("add")){
            response.text = "add";
            inputJSON = inputJSON.substring(3);
        }
        else if (inputJSON.length() > 9 && inputJSON.substring(0,10).equalsIgnoreCase("disconnect")){
            response.text = "disconnect";
            return response;
        }
        else if (inputJSON.length() > 6 && inputJSON.substring(0,7).equalsIgnoreCase("connect")){
            response.text = "connect";
            return response;
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("exit")){
            response.text = "exit";
            return response;
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("help")){
            printHelpMessage();
            response.text = "help";
            return response;
        }
        else if (inputJSON.length() > 5 && inputJSON.substring(0,6).equalsIgnoreCase("import")){
            response.text = "import";
            return response;
        } else {
            response.text = "unknown command : " + inputJSON;
            return response;
        }
        response.values = new LinkedList<Object>();
        try {
            JSONObject JSONobj = new JSONObject(inputJSON);
            response.values.add(getDancerFromJSONObject(JSONobj));
            response.time = Instant.now().getEpochSecond() * 1000000L + (long) Instant.now().getNano() /1000;
            response.login = userLogin;
        } catch (Exception ex){
            try{
                JSONArray JSONarr = new JSONArray(inputJSON);
                for (Object iter : JSONarr){
                    JSONObject JSONobj = (JSONObject)iter;
                    response.values.add(getDancerFromJSONObject(JSONobj));
                    response.time = Instant.now().getEpochSecond() * 1000000L + (long) Instant.now().getNano() /1000;
                    response.login = userLogin;
                }
            } catch (Exception ex2){
                System.out.println(JSONParseException);
                response.text = "JSON wrong";
                response.values = null;
                return response;
            }
        }
        return response;
    }

    public static void main(String[] args) {
        setUserLogin("d3dx13");
        Message msg = getMessageFromJSON("add[[{name : dodo, dynamics: DANCING}, {name : fanta}]");
        System.out.println("=====");
        System.out.println(msg.login);
        System.out.println(msg.time);
        System.out.println(msg.text);
        System.out.println("-----");
        if (msg.values != null)
            for (int i = 0; i < msg.values.size(); i++){
            System.out.println(msg.values.get(i));
        }
        System.out.println("=====");
    }

    private static void printHelpMessage(){
        StringBuffer stringBuffer = new StringBuffer()
                .append("\n--- Commands ---\n")
                .append("help - Вывести в стандартный поток вывода помощь по командам\n")
                .append("exit - Закрыть программу\n")
                .append("connect - выполнить подключение к серверу и получить сессионный AES256 ключ\n")
                .append("disconnect - выполнить корректное отключение от сервера и уничтожить сессионный AES256 ключ\n")
                .append("import - загрузить элементы коллекции из файла по пути переменной окружения COLLECTION_PATH в коллекцию на сервере\n")
                .append("add {...} - Добавить новый элемент в коллекцию\n")
                .append("add_if_min {...} - Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n")
                .append("add_if_max {...} - Добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n")
                .append("remove {...} - Удалить элемент из коллекции по его значению\n")
                .append("show - Вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n")
                .append("save - Сохранить коллекцию в файл\n")
                .append("load - Загрузить коллекцию из файла\n")
                .append("info - Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, дата последнего изменения, количество элементов)\n")
                .append("\n\n--- JSON params ---\n")
                .append("--- [ЗНАЧЕНИЕ] : [описание] ---\n")
                .append("-------------------\n")
                .append("=== name ===\n")
                .append("====== String : Строка с именем\n")
                .append("============\n")
                .append("=== danceQuality ===\n")
                .append("====== int : Начальное количество \"dance points\"\n")
                .append("====================\n");
        stringBuffer.append("=== dynamics ===\n");
        for (PositionState iter : PositionState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("================\n");
        stringBuffer.append("=== feel ===\n");
        for (FeelState iter : FeelState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("============\n");
        stringBuffer.append("=== think ===\n");
        for (ThinkState iter : ThinkState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("=============\n");
        stringBuffer.append("=== position ===\n");
        for (PositionState iter : PositionState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("=============\n");
        System.out.println(stringBuffer);
    }

    private static final StringBuffer JSONParseException = new StringBuffer()
            .append("\n")
            .append("\n")
            .append("\n")
            .append("[ERROR, БУНД!!!]\n")
            .append("Вот как можно взять и неверно ввести JSON?\n")
            .append("Почитайте: https://medium.com/@stasonmars/%D0%B2%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B2-json-c798d2723107\n")
            .append("...\n")
            .append("Ладно, все мы ленивые...\n")
            .append("JSON начинается на { и заканчивается на }\n")
            .append("Внутри этих скобочек через запятую (',' - вот этот знак)\n")
            .append("Пишутся ваши команды. И, нет, ковычки - не часть знака.\n")
            .append("Вид команды таков:\n")
            .append("\"[ИМЯ ПОЛЯ]\" : \"[ЗНАЧЕНИЕ ПОЛЯ]\"\n")
            .append("Обратите внимание на двойные кавычки, и на то, что имя и значение отделены двоеточием.\n")
            .append("Итак, данного инструментала хватит для работы с данной программой. но если вам не лень потратить 15 минут,\n")
            .append("Советую прочитать статью и понять, как много можно в JSON.\n")
            .append("...\n")
            .append("Вот пример для тех, кто сразу смотрит вниз собщения:\n")
            .append("add {\"name\" : \"Ricardo Milos\", \"dynamics\" : \"DANCING\"}\n")
            .append("Обратите внимание, что последня версия программы поддерживает управление несколькими элементами\n")
            .append("Вы можете ввести список ваших объектов через \',\' внутри [...]:\n")
            .append("add [{\"name\" : \"Pen\"}, {\"name\" : \"Pineapple\"}, {\"name\" : \"Apple\"}, {\"name\" : \"Pen\"}, {\"name\" : \"PIKOTARO\", \"dynamics\" : \"DANCING\"}]\n");
}
