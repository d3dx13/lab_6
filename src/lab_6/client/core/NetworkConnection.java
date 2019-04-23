package lab_6.client.core;

import lab_6.message.Message;
import lab_6.message.loggingIn.AuthenticationRequest;
import lab_6.message.loggingIn.AuthenticationResponse;
import lab_6.message.loggingIn.IdentificationRequest;
import lab_6.message.loggingIn.IdentificationResponse;
import lab_6.message.registration.RegistrationRequest;
import lab_6.message.registration.RegistrationResponse;
import lab_6.crypto.ObjectCryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class NetworkConnection {
    public static void command(Message message) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        objectSend(objectCryption.messageEncrypt(message));
    }
    public static void connect(String hostname, int port) throws IOException {
        serverAddress = new InetSocketAddress(hostname, port);
        try {
            objectSend(objectCryption.messageEncrypt(objectCryption.getNewMessage("connect")));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static void disconnect() throws IOException {
        objectSend(objectCryption.getNewMessage("disconnect"));
    }
    public static void status() throws IOException {
        objectSend(objectCryption.getNewMessage("status"));
    }

    protected static void objectSend(Object message) throws IOException {
        server = SocketChannel.open(serverAddress);
        ByteBuffer buffer = ByteBuffer.wrap(objectCryption.messageSerialize(message));
        server.write(buffer);
        buffer.clear();
        server.close();
    }
    protected static IdentificationResponse identification (IdentificationRequest request){

        return new IdentificationResponse();
    }
    protected static RegistrationResponse registration (RegistrationRequest request){

        return new RegistrationResponse();
    }
    protected static AuthenticationResponse authentication (AuthenticationRequest request){

        return new AuthenticationResponse();
    }
    protected static SocketChannel server;
    protected static InetSocketAddress serverAddress;
    public static ObjectCryption objectCryption = new ObjectCryption();
}
