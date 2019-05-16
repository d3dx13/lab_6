package lab_6.client.userInterface;

import lab_6.client.core.CommandParser;
import lab_6.client.core.FileParser;
import lab_6.client.core.NetworkConnection;
import lab_6.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;

import static lab_6.client.userInterface.TableGUI.printTable;

/**
 * Класс для реализации управления коллекцией на сервере через графический интерфейс клиента.
 * За обработку строковых команд отвечает CommandParser.
 * За сообщения с сервером используется NetworkConnection.
 */
public class ConsoleGUI {
    public static void main() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String command;
                System.out.print(String.format("%s: ", NetworkConnection.objectCryption.getUserLogin()));
                command = reader.readLine();
                if (!command.trim().equals("")) {
                    Message message = CommandParser.getMessageFromJSON(command);
                    if (message.text.length() > 5 && message.text.substring(0, 6).equals("import")) {
                        Message response = FileParser.getMessageFromXMLFile(System.getenv().get("COLLECTION_PATH"));
                        NetworkConnection.command(response);
                        continue;
                    }
                    Message response = NetworkConnection.command(message);
                    if (response.text == null)
                        continue;
                    if (response.text.length() > 3 && response.text.substring(0, 4).equals("help"))
                        System.out.println(response.text.substring(4));
                    else if (response.text.length() > 9 && response.text.substring(0, 10).equals("disconnect")) {
                        System.out.println("user disconnected");
                        break;
                    } else if (response.text.length() > 3 && response.text.substring(0, 4).equals("show")) {
                        printTable(response);
                    } else
                        System.out.println(response.text);
                }
            } catch (StreamCorruptedException ex) {
                System.out.println("Package is damaged");
            } catch (IllegalArgumentException ex){
                System.out.println("Путь к файлу не найден. Возможно переменная окружения \"COLLECTION_PATH\" не задана или имеет неправильный путь к файлу." +
                        "+\nПомогите Даше путешественнице найти файл.");

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
    }
}
