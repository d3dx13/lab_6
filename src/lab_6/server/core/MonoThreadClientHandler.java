package lab_6.server.core;
import lab_6.message.*;
import lab_6.message.loggingIn.*;
import lab_6.message.registration.*;
import lab_6.crypto.ObjectCryption;
import lab_6.world.creation.Dancer;
import lab_6.world.state.FeelState;
import lab_6.world.state.PositionState;
import lab_6.world.state.ThinkState;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import static lab_6.Settings.*;
import static lab_6.server.Database.*;


public class MonoThreadClientHandler implements Runnable {
    private Socket clientDialog;
    private ObjectCryption objectCryption;
    public MonoThreadClientHandler(Socket client) {
        this.clientDialog = client;
        this.objectCryption = new ObjectCryption();
    }
    @Override
    public void run() {
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        try {
            objectOutputStream = new ObjectOutputStream(clientDialog.getOutputStream());
            objectInputStream = new ObjectInputStream(clientDialog.getInputStream());
            Object message = objectInputStream.readObject();
            if (message.getClass().equals(Crypted.class)){
                Crypted crypted = (Crypted) message;
                if (!accounts.containsKey(crypted.login) || accounts.get(crypted.login).secretKey == null)
                    throw new IOException();
                this.objectCryption.setSecretKey(accounts.get(crypted.login).secretKey);
                Message request = objectCryption.messageDecrypt(crypted);
                objectOutputStream.writeObject(objectCryption.messageEncrypt(command(request)));
                objectOutputStream.flush();
            }
            else if (message.getClass().equals(RegistrationRequest.class)){
                objectOutputStream.writeObject(registration((RegistrationRequest)message));
                objectOutputStream.flush();
            }
            else if (message.getClass().equals(IdentificationRequest.class)){
                objectOutputStream.writeObject(identification((IdentificationRequest)message));
                objectOutputStream.flush();
            }
            else if (message.getClass().equals(AuthenticationRequest.class)){
                objectOutputStream.writeObject(authentication((AuthenticationRequest) message));
                objectOutputStream.flush();
            }
            objectInputStream.close();
            objectOutputStream.close();
            clientDialog.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message command(Message message){
        Account user = accounts.get(message.login);
        if (message.time > user.lastAccessTime) {
            user.lastAccessTime = message.time;
            accounts.put(message.login, user);
        }
        else
            return objectCryption.getNewMessage("Hello from the Mesozoic");
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

    private RegistrationResponse registration(RegistrationRequest request){
        RegistrationResponse response = new RegistrationResponse();
        if (request.login.length() < loginMinimalLength){
            response.confirm = false;
            response.message = "login is short";
            return response;
        }
        if (request.login.length() > loginMaximalLength){
            response.confirm = false;
            response.message = "login is long";
            return response;
        }
        Account tempAccount = new Account();
        tempAccount.login = request.login;
        tempAccount.publicKey = request.publicKey.clone();
        tempAccount.privateKey = request.privateKey.clone();
        tempAccount.registrationDate = (new Date()).toString();
        if (!accounts.containsKey(request.login)){
            accounts.putIfAbsent(request.login, tempAccount);
            accountsSave();
            response.confirm = true;
            response.message = "success";
        } else {
            response.confirm = false;
            response.message = "user exists";
        }
        return response;
    }

    private IdentificationResponse identification(IdentificationRequest request) {
        IdentificationResponse response = new IdentificationResponse();
        try {
            if (request.login.length() < loginMinimalLength) {
                response.message = "login is short";
                return response;
            }
            if (request.login.length() > loginMaximalLength) {
                response.message = "login is long";
                return response;
            }
            if (!accounts.containsKey(request.login)) {
                response.message = "login is wrong";
                return response;
            }
            Account user = accounts.get(request.login);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(user.publicKey)));
            SecureRandom secureRandom = new SecureRandom();
            user.random = new byte[identificationRandomSize];
            secureRandom.nextBytes(user.random);
            response.random = cipher.doFinal(user.random);
            response.privateKey = user.privateKey.clone();
            accounts.put(request.login, user);
            response.message = "success";
            return response;
        } catch (Exception ex){
            response.message = ex.getMessage();
            return response;
        }
    }

    private AuthenticationResponse authentication(AuthenticationRequest request){
        AuthenticationResponse response = new AuthenticationResponse();
        if (request.login.length() < loginMinimalLength) {
            response.message = "login is short";
            return response;
        }
        if (request.login.length() > loginMaximalLength) {
            response.message = "login is long";
            return response;
        }
        if (!accounts.containsKey(request.login)) {
            response.message = "login is wrong";
            return response;
        }
        if (!(Arrays.equals(accounts.get(request.login).random, request.random))) {
            response.message = "random is wrong";
            return response;
        } else {
            try {
                Account user = accounts.get(request.login);
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(user.publicKey)));
                SecureRandom secureRandom = new SecureRandom();
                user.secretKey = new byte[userAESKeySize];
                secureRandom.nextBytes(user.secretKey);
                response.secretKey = cipher.doFinal(user.secretKey);
                accounts.put(request.login, user);
                response.message = "success";
                return response;
            } catch (Exception ex){
                response.message = ex.getMessage();
                return response;
            }
        }
    }

    private Message disconnect(Message message){
        Account user = accounts.get(message.login);
        user.lastAccessTime = 0;
        user.secretKey = null;
        user.random = null;
        accounts.put(message.login, user);
        return objectCryption.getNewMessage("disconnect");
    }

    private Message getHelpMessage(){
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
    private Message show(){
        Message response = new Message();
        response.text = "show";
        collectionData.stream().sorted().forEachOrdered(dancer -> response.values.addLast(dancer));
        return response;
    }
    private Message add(Message request){
        Message response = new Message();
        response.text = "add success";
        request.values.parallelStream().map(o -> (Dancer)o).forEach(dancer -> collectionData.add(dancer));
        collectionInfo.lastChangeTime = Date.from(Instant.now()).toString();
        return response;
    }
    private Message add_if_max(Message request){
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
    private Message add_if_min(Message request){
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
    private Message remove(Message request){
        Message response = new Message();
        request.values.parallelStream().map(o -> (Dancer)o).forEach(o -> collectionData.remove(o));
        response.text = "remove success";
        collectionInfo.lastChangeTime = Date.from(Instant.now()).toString();
        return response;
    }
    private Message save(){
        Message response = new Message();
        if (collectionSave())
            response.text = "save success";
        else
            response.text = "save failed";
        return response;
    }
    private Message load(){
        Message response = new Message();
        response.text = "load success";
        if (collectionLoad())
            response.text = "load success";
        else
            response.text = "load failed";
        return response;
    }
    private Message info(){
        Message response = new Message();
        response.text = "info\n" + getInfo();
        return response;
    }
}