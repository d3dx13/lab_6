package lab_6.client;

import lab_6.client.core.CommandParser;
import lab_6.client.core.NetworkConnection;
import lab_6.client.userInterface.ConsoleGUI;

public class Client {
    public static void main(String[] args) {
        try {
            CommandParser.setUserLogin("d3dx13");
            NetworkConnection.objectCryption.setUserLogin("d3dx13");
            NetworkConnection.objectCryption.setSecretKey("12345678901234567890123456789012".getBytes());
            NetworkConnection.connect("localhost", 8000);
            ConsoleGUI.main("d3dx13");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
