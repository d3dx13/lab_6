package lab_6.client.core;

import lab_6.message.Message;
import lab_6.world.creation.Dancer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;

/**
 * Класс, реализующий парсинг команд на стороне клиента.
 */
public class CommandParser {
    private static String userLogin;

    /**
     * Метод, установливающий логин клиенту.
     * @param login Логин, который устанавливается клиенту.
     */
    public static void setUserLogin(String login){
        userLogin = login;
    }

    /**
     * Метод, который переводит строку, заданную в JSON-формате, в объект Dancer.
     * @param JSONobj JSONObject объект, который переводится в объект Dancer.
     * @return Объект Dancer, который возвращает метод, получив его из JSON.
     */
    public static Dancer getDancerFromJSONObject(JSONObject JSONobj){
        Dancer tempDancer = new Dancer("NoName");
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

    /**
     * Метод, который переводит строку, заданную в JSON-формате, в объект Message.
     * @param inputJSON JSONObject объект, который переводится в объект Message.
     * @return Объект Message, который возвращает метод, получив его из JSON.
     */
    public static Message getMessageFromJSON(String inputJSON){
        Message response = NetworkConnection.objectCryption.getNewMessage();
        inputJSON = inputJSON.trim();
        if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("show")){
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
        else if (inputJSON.length() > 3 && inputJSON.substring(0,4).equalsIgnoreCase("help")){
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

    /**
     * Исключение, которое возникает при ошибке в обработке JSON. Оно содержит рекомендаци, позволяющие исправить ошибку.
     */
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
