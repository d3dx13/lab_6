package lab_6.server.ThreadServer;
import lab_6.message.Account;
import lab_6.message.Message;
import lab_6.message.loggingIn.*;
import lab_6.message.registration.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static lab_6.server.Database.*;
import static lab_6.server.Settings.*;


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
                objectOutputStream.writeObject(command(msg));
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
        }catch (SocketException e){
            System.out.println(clientDialog.toString() + " disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message command(Message message){
        return new Message();
    }

    private RegistrationResponse registration(RegistrationRequest request){
        RegistrationResponse response = new RegistrationResponse();
        if (request.login.length() < loginMinimalLength){
            response.confirm = false;
            response.message = "login short";
            return response;
        }
        if (request.login.length() > loginMaximalLength){
            response.confirm = false;
            response.message = "login long";
            return response;
        }
        Account tempAccount = new Account();
        tempAccount.login = request.login;
        tempAccount.publicKey = request.publicKey.clone();
        tempAccount.privateKey = request.privateKey.clone();
        tempAccount.registrationDate = (new Date()).toString();
        if (!accounts.containsKey(request.login)){
            accounts.putIfAbsent(request.login, tempAccount);
            response.confirm = true;
            response.message = "user created";
        } else {
            response.confirm = false;
            response.message = "user exist";
        }
        return response;
    }

    private IdentificationResponse identification(IdentificationRequest request){
        return new IdentificationResponse();
    }

    private AuthenticationResponse authentication(AuthenticationRequest request){
        return new AuthenticationResponse();
    }
}