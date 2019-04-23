package lab_6.client.userInterface;

import lab_6.client.core.CommandParser;
import lab_6.client.core.NetworkConnection;
import lab_6.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleGUI {
    public static void main(String userLogin) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit) {
            try {
                String command;
                System.out.print(String.format("%s: ", userLogin));
                command = reader.readLine();
                if (!command.strip().equals("")) {
                    Message response = NetworkConnection.command(CommandParser.getMessageFromJSON(command));
                    System.out.println(response.text);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                exit = true;
            }
        }
    }
}
