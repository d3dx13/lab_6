package lab_6.server.TestServer;

import java.io.*;
import java.net.Socket;

public class TestRunnableClientTester implements Runnable {

    private Object object;
    private Socket socket;

    public TestRunnableClientTester(Object object) {
        try {
            this.object = object;
            socket = new Socket("localhost", 8000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            System.out.println("Client oos & ois initialized");

            int i = 0;

            while (i < 5) {
                oos.writeObject(this.object);
                oos.flush();
                Object in = ois.readObject();
                System.out.println(in);
                i++;
            }
        } catch (Exception e) {
        }
    }
}
