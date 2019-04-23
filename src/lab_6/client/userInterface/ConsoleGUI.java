package lab_6.client.userInterface;

import lab_6.client.core.CommandParser;
import lab_6.client.core.NetworkConnection;
import lab_6.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleGUI {
    public static void main() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String command;
                System.out.print(String.format("%s: ", NetworkConnection.objectCryption.getUserLogin()));
                command = reader.readLine();
                if (!command.strip().equals("")) {
                    Message message = CommandParser.getMessageFromJSON(command);
                    if (message.text.length() > 5 && message.text.substring(0,6).equals("import")){
                    }
                    Message response = NetworkConnection.command(message);
                    if (response.text.length() > 3 && response.text.substring(0, 4).equals("help"))
                        System.out.println(response.text.substring(4));
                    else if (response.text.length() > 9 && response.text.substring(0, 10).equals("disconnect")) {
                        System.out.println("user disconnected");
                        break;
                    }
                    else if (response.text.length() > 3 && response.text.substring(0, 4).equals("show")) {
                        System.out.println(response.values);
                        if (response.values != null)
                            response.values.forEach(o -> System.out.println("> " + o));
                        System.out.println(response.text);
                    }
                    else if (response.text.length() > 2 && response.text.substring(0, 3).equals("add")) {
                        System.out.println(response.text);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
    }
}
