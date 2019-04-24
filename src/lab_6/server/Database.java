package lab_6.server;

import lab_6.crypto.ObjectCryption;
import lab_6.message.Account;
import lab_6.message.CollectionInfo;
import lab_6.world.creation.Dancer;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class Database {
    public static File collectionPath = new File("src/lab_6/server/database/");
    public static PriorityBlockingQueue<Dancer> collectionData = new PriorityBlockingQueue<Dancer>();
    public static CollectionInfo collectionInfo = new CollectionInfo();
    public static ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<String, Account>();
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
    public synchronized static boolean collectionLoad(){
        try {
            FileInputStream fileInputStream;
            ObjectCryption objectCryption = new ObjectCryption();
            File dataPath = new File(collectionPath.getPath() + "/data");
            if (!dataPath.exists())
                collectionSave();
            fileInputStream = new FileInputStream(dataPath);
            collectionData = (PriorityBlockingQueue<Dancer>)objectCryption.messageDeserialize(fileInputStream.readAllBytes());
            fileInputStream.close();
            dataPath = new File(collectionPath.getPath() + "/info");
            if (!dataPath.exists())
                collectionSave();
            fileInputStream = new FileInputStream(dataPath);
            collectionInfo = (CollectionInfo)objectCryption.messageDeserialize(fileInputStream.readAllBytes());
            fileInputStream.close();
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
    public synchronized static boolean accountsLoad(){
        try {
            FileInputStream fileInputStream;
            ObjectCryption objectCryption = new ObjectCryption();
            File dataPath = new File(collectionPath.getPath() + "/accounts");
            if (!dataPath.exists())
                accountsSave();
            fileInputStream = new FileInputStream(dataPath);
            accounts = (ConcurrentHashMap<String, Account>)objectCryption.messageDeserialize(fileInputStream.readAllBytes());
            fileInputStream.close();
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
    public synchronized static String getInfo(){
        collectionInfo.size = collectionData.size();
        return collectionInfo.toString();
    }
}
