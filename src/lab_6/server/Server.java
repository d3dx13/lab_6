package lab_6.server;

import lab_6.server.core.MultiThreadServer;

import static lab_6.server.Database.*;

public class Server {
    public static void main(String[] args) {
        class MyShutdownHook extends Thread {
            public void run() {
                accountsSave();
                collectionSave();
            }
        }
        MyShutdownHook shutdownHook = new MyShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        accountsLoad();
        StringBuffer stringBuffer = new StringBuffer();
        accounts.forEach((s, account) -> stringBuffer
                .append(s)
                .append(" - user found, registered - ")
                .append(account.registrationDate)
                .append("\n"));
        System.out.println(stringBuffer);
        collectionLoad();
        System.out.println(getInfo());
        MultiThreadServer.main();
    }
}
