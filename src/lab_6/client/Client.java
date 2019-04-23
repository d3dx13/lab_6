package lab_6.client;

import lab_6.client.core.CommandParser;
import lab_6.client.core.NetworkConnection;
import lab_6.client.userInterface.ConsoleGUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static lab_6.Settings.loginMaximalLength;
import static lab_6.Settings.loginMinimalLength;

public class Client {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            String hostname = "localhost";
            int port = 8000;

            System.out.print(String.format("\nLogin must be %d to %d characters\nEnter your login: ",loginMinimalLength,loginMaximalLength));
            String login = reader.readLine();
            if (login.length() < loginMinimalLength || login.length() > loginMaximalLength){
                System.out.println("!!! Login must be %d to %d characters !!!");
                return;
            }
            NetworkConnection.objectCryption.setUserLogin(login);

            //NetworkConnection.objectCryption.setSecretKey(secretKey);

            CommandParser.setUserLogin(NetworkConnection.objectCryption.getUserLogin());
            NetworkConnection.setServerAddressr(hostname, port);


            NetworkConnection.signUp();

            NetworkConnection.signIn();

            ConsoleGUI.main(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
