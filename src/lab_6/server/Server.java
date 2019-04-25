package lab_6.server;

import lab_6.server.core.MultiThreadServer;

import static lab_6.server.Database.*;

/**
 * Оболочка многопоточного сервера.
 * Внутри неё считываются данные учётных записей,
 * Поднимается система перехвата сигнала выключения,
 * В оперативную память загружается коллекция,
 * После чего стартует многопоточный сервис-обработчик задач.
 */
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
        StringBuffer stringBuffer = new StringBuffer().append("\n\n");
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
