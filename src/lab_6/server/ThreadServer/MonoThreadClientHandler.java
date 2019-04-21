package lab_6.server.ThreadServer;
import lab_6.message.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class MonoThreadClientHandler implements Runnable {
    private Socket clientDialog;
    public MonoThreadClientHandler(Socket client) {
        this.clientDialog = client;
    }
    @Override
    public void run() {
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        try {
            objectOutputStream = new ObjectOutputStream(clientDialog.getOutputStream());
            objectInputStream = new ObjectInputStream(clientDialog.getInputStream());

            Object message = objectInputStream.readObject();
            if (message.getClass().equals(Message.class)){
                Message msg = (Message) message;
                System.out.println("login: "+ msg.login);
                System.out.println("time: "+ msg.time);
                System.out.println("text: "+ msg.text);
                System.out.print("keys: ");
                if (msg.keys != null)
                    msg.keys.forEach(s -> System.out.print(s+", "));
                System.out.print("\nvalues: ");
                if (msg.values  != null)
                    msg.values.forEach(s -> System.out.print(s+", "));
                System.out.print("\n");
            }



            //System.out.println(entry + " :: " + entry.getClass() + "  ::  " + entry.getClass().equals(Double.class));

            objectOutputStream.writeObject(message);
            objectOutputStream.flush();

            objectInputStream.close();
            objectOutputStream.close();
            clientDialog.close();
        }catch (SocketException e){
            System.out.println(clientDialog.toString() + " disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}