package lab_6.server;

import lab_6.message.Account;
import lab_6.world.Dancer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class Database {
    public static PriorityBlockingQueue<Dancer> collection = new PriorityBlockingQueue<Dancer>();
    public static ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<String, Account>();
    public synchronized static boolean collectionLoad(){
        return false;
    }
    public synchronized static boolean accountsLoad(){
        return false;
    }
    public synchronized static boolean collectionSave(){
        return false;
    }
    public synchronized static boolean accountsSave(){
        return false;
    }
}
