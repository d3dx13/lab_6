package lab_6.server.core;

import lab_6.message.Account;
import lab_6.message.Message;
import lab_6.world.creation.Dancer;
import lab_6.world.state.FeelState;
import lab_6.world.state.PositionState;
import lab_6.world.state.ThinkState;

import java.time.Instant;
import java.util.Date;

import static lab_6.server.Database.*;
import static lab_6.server.Database.collectionLoad;
import static lab_6.server.Database.getInfo;

class CommandHandler {
    static Message command(Message message){
        Account user = accounts.get(message.login);
        if (message.time > user.lastAccessTime) {
            user.lastAccessTime = message.time;
            accounts.put(message.login, user);
        }
        else{
            Message response = new Message();
            response.text = "Hello from the Mesozoic";
            response.login = message.login;
            response.time = message.time;
            return response;
        }
        if (message.text.length() > 3 && message.text.substring(0,4).equals("help"))
            return getHelpMessage();
        if (message.text.length() > 9 && message.text.substring(0,10).equals("disconnect"))
            return disconnect(message);
        if (message.text.length() > 3 && message.text.substring(0,4).equals("show"))
            return show();
        if (message.text.length() > 9 && message.text.substring(0,10).equals("add_if_max"))
            return add_if_max(message);
        if (message.text.length() > 9 && message.text.substring(0,10).equals("add_if_min"))
            return add_if_min(message);
        if (message.text.length() > 2 && message.text.substring(0,3).equals("add"))
            return add(message);
        if (message.text.length() > 5 && message.text.substring(0,6).equals("remove"))
            return remove(message);
        if (message.text.length() > 3 && message.text.substring(0,4).equals("save"))
            return save();
        if (message.text.length() > 3 && message.text.substring(0,4).equals("load"))
            return load();
        if (message.text.length() > 3 && message.text.substring(0,4).equals("info"))
            return info();
        return new Message();
    }
    private static Message disconnect(Message message){
        Account user = accounts.get(message.login);
        user.lastAccessTime = 0;
        user.secretKey = null;
        user.random = null;
        accounts.put(message.login, user);
        Message response = new Message();
        response.text = "disconnect";
        response.login = message.login;
        response.time = message.time;
        return response;
    }
    private static Message getHelpMessage(){
        StringBuffer stringBuffer = new StringBuffer()
                .append("help\n")
                .append("--- Commands ---\n")
                .append("help - Вывести в стандартный поток вывода помощь по командам\n")
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
        Message response = new Message();
        response.text = stringBuffer.toString();
        return response;
    }
    private static Message show(){
        Message response = new Message();
        response.text = "show";
        collectionData.stream().sorted().forEachOrdered(dancer -> response.values.addLast(dancer));
        return response;
    }
    private static Message add(Message request){
        Message response = new Message();
        response.text = "add success";
        request.values.parallelStream().map(o -> (Dancer)o).forEach(dancer -> collectionData.add(dancer));
        collectionInfo.lastChangeTime = Date.from(Instant.now()).toString();
        return response;
    }
    private static Message add_if_max(Message request){
        Message response = new Message();
        if (collectionData.isEmpty()){
            response.text = "add_if_max failed";
            return response;
        }
        Dancer dancerMax = collectionData.stream().max((dancer, t1) -> (dancer.getDanceQuality() - t1.getDanceQuality())).get();
        request.values.parallelStream().map(o -> (Dancer)o).filter(o -> (o.getDanceQuality() >= dancerMax.getDanceQuality())).forEach(dancer -> collectionData.add(dancer));
        response.text = "add_if_max success";
        collectionInfo.lastChangeTime = Date.from(Instant.now()).toString();
        return response;
    }
    private static Message add_if_min(Message request){
        Message response = new Message();
        if (collectionData.isEmpty()){
            response.text = "add_if_min failed";
            return response;
        }
        Dancer dancerMin = collectionData.stream().min((dancer, t1) -> (dancer.getDanceQuality() - t1.getDanceQuality())).get();
        request.values.parallelStream().map(o -> (Dancer)o).filter(o -> (o.getDanceQuality() <= dancerMin.getDanceQuality())).forEach(dancer -> collectionData.add(dancer));
        response.text = "add_if_min success";
        collectionInfo.lastChangeTime = Date.from(Instant.now()).toString();
        return response;
    }
    private static Message remove(Message request){
        Message response = new Message();
        request.values.parallelStream().map(o -> (Dancer)o).forEach(o -> collectionData.remove(o));
        response.text = "remove success";
        collectionInfo.lastChangeTime = Date.from(Instant.now()).toString();
        return response;
    }
    private static Message save(){
        Message response = new Message();
        if (collectionSave())
            response.text = "save success";
        else
            response.text = "save failed";
        return response;
    }
    private static Message load(){
        Message response = new Message();
        response.text = "load success";
        if (collectionLoad())
            response.text = "load success";
        else
            response.text = "load failed";
        return response;
    }
    private static Message info(){
        Message response = new Message();
        response.text = "info\n" + getInfo();
        return response;
    }
}
