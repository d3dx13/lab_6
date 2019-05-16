package lab_6.server;

import lab_6.crypto.ObjectCryption;
import lab_6.message.Account;
import lab_6.message.CollectionInfo;
import lab_6.world.creation.Dancer;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import static lab_6.Settings.databasePath;


/**
 * Главный класс для работы с коллекциями аккаунтов и "Танцоров".
 * Позволяет получать информацию о коллекции,
 * А также безопасно её сохранять/загружать.
 * Также позволяет обновлять данные учётных записей.
 */
public class Database {
    /**
     * Путь к директории с текущей коллекцией.
     */
    public static File collectionPath = new File(databasePath);
    /**
     * Коллекция "Танцоров".
     */
    public static PriorityBlockingQueue<Dancer> collectionData = new PriorityBlockingQueue<Dancer>();
    /**
     * Информация о коллекции "Танцоров".
     */
    public static CollectionInfo collectionInfo = new CollectionInfo();
    /**
     * Информация о учётных записях пользователей.
     */
    public static ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<String, Account>();
    /**
     * Сохранить коллекцию.
     * Операция потокобезопасна.
     * @return Успешность операции.
     */
    public synchronized static boolean collectionSave(){
        try {
            FileOutputStream fileOutputStream;
            ObjectCryption objectCryption = new ObjectCryption();
            File dataPath = new File(collectionPath.getPath() + "/data");
            fileOutputStream = new FileOutputStream(dataPath, false);
            fileOutputStream.write(objectCryption.messageSerialize(collectionData));
            fileOutputStream.close();
            dataPath = new File(collectionPath.getPath() + "/info");
            fileOutputStream = new FileOutputStream(dataPath, false);
            collectionInfo.size = collectionData.size();
            collectionInfo.type = "Dancer";
            fileOutputStream.write(objectCryption.messageSerialize(collectionInfo));
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * Загрузить коллекцию.
     * Операция потокобезопасна.
     * @return Успешность операции.
     */
    public synchronized static boolean collectionLoad(){
        try {
            FileInputStream fileInputStream;
            ObjectCryption objectCryption = new ObjectCryption();
            File dataPath = new File(collectionPath.getPath() + "/data");
            if (!dataPath.exists())
                collectionSave();
            collectionData = (PriorityBlockingQueue<Dancer>)objectCryption.messageDeserialize(Files.readAllBytes(dataPath.toPath()));
            dataPath = new File(collectionPath.getPath() + "/info");
            if (!dataPath.exists())
                collectionSave();
            collectionInfo = (CollectionInfo)objectCryption.messageDeserialize(Files.readAllBytes(dataPath.toPath()));
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * Сохранить данные учётных записей.
     * Операция потокобезопасна.
     * @return Успешность операции.
     */
    public synchronized static boolean accountsSave(){
        try {
            FileOutputStream fileOutputStream;
            ObjectCryption objectCryption = new ObjectCryption();
            File dataPath = new File(collectionPath.getPath() + "/accounts");
            fileOutputStream = new FileOutputStream(dataPath, false);
            fileOutputStream.write(objectCryption.messageSerialize(accounts));
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * Загрузить данные учётных записей.
     * Операция потокобезопасна.
     * @return Успешность операции.
     */
    public synchronized static boolean accountsLoad(){
        try {
            ObjectCryption objectCryption = new ObjectCryption();
            File dataPath = new File(collectionPath.getPath() + "/accounts");
            if (!dataPath.exists())
                accountsSave();
            accounts = (ConcurrentHashMap<String, Account>)objectCryption.messageDeserialize(Files.readAllBytes(dataPath.toPath()));
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * Метод для получения информации о коллекции.
     * @return Строка с информацией о коллекции.
     */
    public synchronized static String getInfo(){
        collectionInfo.size = collectionData.size();
        return collectionInfo.toString();
    }
}
