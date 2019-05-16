package lab_6.server.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static lab_6.Settings.ApplicationPort;
import static lab_6.Settings.threadPoolSize;

/**
 * Запускает запрос пользователя на исполнение в одном из потоков FixedThreadPool.
 * В основе лежит ExecutorService.
 */
public class MultiThreadServer {
    /**
     * Пул потоков для выполнения запросов пользователя.
     * Размер пула: threadPoolSize.
     */
    static ExecutorService executeIt = Executors.newFixedThreadPool(threadPoolSize);
    public static void main() {
        try {
            ServerSocket server = new ServerSocket(ApplicationPort);
            while (!server.isClosed()) {
                Socket client = server.accept();
                executeIt.execute(new MonoThreadClientHandler(client));
            }
            executeIt.shutdown();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
