package lab_6.server;

import lab_6.message.Account;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    public static ConcurrentHashMap<String, Object> collection = new ConcurrentHashMap<String, Object>();
    public static ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<String, Account>();
}
