package lab_6.client.core;

import lab_6.message.Message;
import lab_6.world.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.LinkedList;
import java.util.Map;

public class CommandParser {
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
    public static Message getMessageFromJSON(String inputJSON, String userLogin){
        Message response = new Message();
        inputJSON = inputJSON.strip();
        if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("show")){
            response.text = "show";
            inputJSON = inputJSON.substring(4);
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("save")){
            response.text = "save";
            inputJSON = inputJSON.substring(4);
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("load")){
            response.text = "load";
            inputJSON = inputJSON.substring(4);
        }
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("info")){
            response.text = "info";
            inputJSON = inputJSON.substring(4);
        }
        else if (inputJSON.length() > 12 && inputJSON.substring(0,13).equalsIgnoreCase("remove_higher")){
            response.text = "remove_higher";
            inputJSON = inputJSON.substring(13);
        }
        else if (inputJSON.length() > 11 && inputJSON.substring(0,12).equalsIgnoreCase("remove_lower")){
            response.text = "remove_lower";
            inputJSON = inputJSON.substring(12);
        }
        else if (inputJSON.length() > 5 && inputJSON.substring(0,6).equalsIgnoreCase("remove")){
            response.text = "remove";
            inputJSON = inputJSON.substring(6);
        }
        else if (inputJSON.length() > 5 && inputJSON.substring(0,6).equalsIgnoreCase("insert")){
            response.text = "insert";
            inputJSON = inputJSON.substring(6);
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
        }
        response.keys = new LinkedList<String>();
        response.values = new LinkedList<Object>();
        try {
            JSONObject JSONobj = new JSONObject(inputJSON);
            if (JSONobj.keySet().contains("key") && JSONobj.keySet().contains("value")){
                response.keys.add(JSONobj.get("key").toString());
                response.values.add(getDancerFromJSONObject((JSONObject) JSONobj.get("value")));
            }
            else if (JSONobj.keySet().contains("key") && !JSONobj.keySet().contains("value")) {
                response.keys.add(JSONobj.get("key").toString());
                response.values.add(null);
            }
            else if (!JSONobj.keySet().contains("key") && JSONobj.keySet().contains("value")) {
                response.keys.add(null);
                response.values.add(getDancerFromJSONObject((JSONObject) JSONobj.get("value")));
            }
            else {
                response.keys.add(null);
                response.values.add(null);
            }
            response.time = Instant.now().getEpochSecond() * 1000000L + (long) Instant.now().getNano() /1000;
            response.login = userLogin;
        } catch (Exception ex){
            try{
                JSONArray JSONarr = new JSONArray(inputJSON);
                for (Object iter : JSONarr){
                    JSONObject JSONobj = (JSONObject)iter;
                    if (JSONobj.keySet().contains("key") && JSONobj.keySet().contains("value")){
                        response.keys.add(JSONobj.get("key").toString());
                        response.values.add(getDancerFromJSONObject((JSONObject) JSONobj.get("value")));
                    }
                    else if (JSONobj.keySet().contains("key") && !JSONobj.keySet().contains("value")) {
                        response.keys.add(JSONobj.get("key").toString());
                        response.values.add(null);
                    }
                    else if (!JSONobj.keySet().contains("key") && JSONobj.keySet().contains("value")) {
                        response.keys.add(null);
                        response.values.add(getDancerFromJSONObject((JSONObject) JSONobj.get("value")));
                    }
                    else {
                        response.keys.add(null);
                        response.values.add(null);
                    }
                    response.time = Instant.now().getEpochSecond() * 1000000L + (long) Instant.now().getNano() /1000;
                    response.login = userLogin;
                }
            } catch (Exception ex2){
                System.out.println(JSONParseException);
                response.text = "command incorrect";
                response.values = null;
                response.keys = null;
                return response;
            }
        }
        return response;
    }

    public static void main(String[] args) {
        Message msg = getMessageFromJSON("help[{key:\"1asdw23\", value:{name : dddodd, awda: wdawawd}}, {key:\"dwa\", value:{name : faawa}}]", "d3dx13");
        System.out.println("=====");
        System.out.println(msg.login);
        System.out.println(msg.time);
        System.out.println(msg.text);
        System.out.println("-----");
        if (msg.keys != null && msg.values != null)
            for (int i = 0; i < msg.keys.size(); i++){
            System.out.println(msg.keys.get(i) + " : " + msg.values.get(i));
        }
        System.out.println("=====");
    }

    private static void printHelpMessage(){
        StringBuffer stringBuffer = new StringBuffer()
                .append("\n--- Commands ---\n")
                .append("help - Вывести в стандартный поток вывода помощь по командам\n")
                .append("exit - Закрыть программу\n")
                .append("connect - \n")
                .append("disconnect - \n")
                .append("import - \n")
                .append("insert {key:String, value{}} - \n")
                .append("remove {key:String, value{}} or {key:String} or {value:{}} - \n")
                .append("remove_lower {key:String} or {value:{}} - \n")
                .append("remove_higher {key:String} or {value:{}} - \n")
                .append("show - Вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n")
                .append("save - Сохранить коллекцию в файл\n")
                .append("load - Загрузить коллекцию из файла\n")
                .append("info - \n")
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
            .append("Но конкретно эта программа толекантна к отсутствию СРАЗУ ДВУХ кавычек у имени поля или его значения.\n")
            .append("Итак, данного инструментала хватит для работы с данной программой. но если вам не лень потратить 15 минут,\n")
            .append("Советую прочитать статью и понять, как много можно в JSON.\n")
            .append("...\n")
            .append("Вот пример для тех, кто сразу смотрит вниз собщения:\n")
            .append("{\"name\" : \"Ricardo Milos\", \"dynamics\" : \"DANCING\"}\n")
            .append("\n");
}
