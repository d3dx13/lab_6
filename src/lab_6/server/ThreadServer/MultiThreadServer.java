package lab_6.server.ThreadServer;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(12);
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8000);
            while (!server.isClosed()) {
                Socket client = server.accept();
                executeIt.execute(new MonoThreadClientHandler(client));
            }
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}