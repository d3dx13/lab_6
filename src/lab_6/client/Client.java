package lab_6.client;

import lab_6.client.userInterface.ConsoleGUI;
import lab_6.message.Message;
import lab_6.world.creation.Dancer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;

public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel crunchifyClient = SocketChannel.open(new InetSocketAddress("localhost", 8000));

            Message message = new Message();
            message.text = "TEST";
            message.values = new LinkedList<Object>();
            for (int i = 0; i < 10000;i++)
                message.values.addLast(new Dancer("id " + i));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;

            out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
            byte[] byteObject = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(byteObject);
            crunchifyClient.write(buffer);
            buffer.clear();
            System.out.println(byteObject.length);


            crunchifyClient.close();

            ConsoleGUI.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
