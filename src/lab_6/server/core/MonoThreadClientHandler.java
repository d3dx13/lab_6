package lab_6.server.core;
import lab_6.message.*;
import lab_6.message.loggingIn.*;
import lab_6.message.registration.*;
import lab_6.crypto.ObjectCryption;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static lab_6.server.Database.*;
import static lab_6.server.core.SignUpInHandler.*;
import static lab_6.server.core.CommandHandler.*;

/**
 * Запускаемый Runnable поток для обработки запроса пользователя.
 * Передаёт команды в CommandHandler и SignUpInHandler.
 */
public class MonoThreadClientHandler implements Runnable {
    private Socket clientDialog;
    private ObjectCryption objectCryption;
    MonoThreadClientHandler(Socket client) {
        this.clientDialog = client;
        this.objectCryption = new ObjectCryption();
    }
    @Override
    public void run() {
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        try {
            objectOutputStream = new ObjectOutputStream(clientDialog.getOutputStream());
            objectInputStream = new ObjectInputStream(clientDialog.getInputStream());
            Object message = objectInputStream.readObject();
            if (message.getClass().equals(Crypted.class)){
                Crypted crypted = (Crypted) message;
                if (!accounts.containsKey(crypted.login) || accounts.get(crypted.login).secretKey == null)
                    throw new IOException();
                this.objectCryption.setSecretKey(accounts.get(crypted.login).secretKey);
                Message request = objectCryption.messageDecrypt(crypted);
                objectOutputStream.writeObject(objectCryption.messageEncrypt(command(request)));
                objectOutputStream.flush();
            }
            else if (message.getClass().equals(RegistrationRequest.class)){
                objectOutputStream.writeObject(registration((RegistrationRequest)message));
                objectOutputStream.flush();
            }
            else if (message.getClass().equals(IdentificationRequest.class)){
                objectOutputStream.writeObject(identification((IdentificationRequest)message));
                objectOutputStream.flush();
            }
            else if (message.getClass().equals(AuthenticationRequest.class)){
                objectOutputStream.writeObject(authentication((AuthenticationRequest) message));
                objectOutputStream.flush();
            }
            objectInputStream.close();
            objectOutputStream.close();
            clientDialog.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}