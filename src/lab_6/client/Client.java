package lab_6.client;

import lab_6.client.core.CommandParser;
import lab_6.client.core.NetworkConnection;
import lab_6.client.userInterface.ConsoleGUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static lab_6.Settings.loginMaximalLength;
import static lab_6.Settings.loginMinimalLength;
import static lab_6.client.core.NetworkConnection.getServerAddressr;

/**
 * Оболочка клиента.
 * Реализует консольный интерфейс для работы в Offline режиме.
 */
public class Client {
    private static BufferedReader reader;
    public static void main(String[] args) {
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            login();
            while (true){
                try {
                    System.out.print(String.format("[offline] Address{%s} : %s :>>> ", getServerAddressr(), NetworkConnection.objectCryption.getUserLogin()));
                    String command = reader.readLine().trim();
                    if (command.length() > 3 && command.substring(0, 4).equals("help")) {
                        help();
                    } else if (command.length() > 4 && command.substring(0, 5).equals("login")) {
                        login();
                    } else if (command.length() > 6 && command.substring(0, 7).equals("connect")) {
                        connect();
                    } else if (command.length() > 11 && command.substring(0, 12).equals("registration")) {
                        registration();
                    } else if (command.length() > 5 && command.substring(0, 6).equals("server")) {
                        server();
                    } else if (command.length() > 3 && command.substring(0, 4).equals("exit")) {
                        break;
                    }
                } catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Выбор сервера с коллекцией.
     */
    private static void server(){
        String hostname;
        int port;
        try {
            System.out.print("hostname: ");
            hostname = reader.readLine();
            System.out.print("port: ");
            port = Integer.valueOf(reader.readLine());
        } catch (Exception ex){
            System.out.println("В поле \"port\" нужно ввести порт сервера\nЭто целое число от 1 до 65535");
            return;
        }
        NetworkConnection.setServerAddressr(hostname, port);
    }
    /**
     * Помощь по командам.
     */
    private static void help(){
        System.out.println(new StringBuffer()
                .append("\nOffline mode:\n")
                .append("login - Настроить имя пользователя\n")
                .append("server - Настроить адрес и порт сервера\n")
                .append("connect - Зайти на сервер под своей учётной записью\n")
                .append("registration - Создать новую учётную запись на сервере\n")
                .append("exit - Выход из программы\n")
        );
    }
    /**
     * Выбор имени пользователя.
     */
    private static void login() {
        System.out.print(String.format("\nLogin must be %d to %d characters\nEnter your login: ",loginMinimalLength,loginMaximalLength));
        String login;
        try {
            login = reader.readLine();
        } catch (Exception ex){
            return;
        }
        if (login.length() < loginMinimalLength || login.length() > loginMaximalLength){
            System.out.println(String.format("!!! Login must be %d to %d characters !!!", loginMinimalLength, loginMaximalLength));
            return;
        }
        NetworkConnection.objectCryption.setUserLogin(login);
        CommandParser.setUserLogin(login);
    }
    /**
     * Попытка подключения к серверу.
     */
    private static void connect()  {
        if (getServerAddressr() == null)
            server();
        try {
            if (NetworkConnection.signIn())
                ConsoleGUI.main();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Попытка регистрации на сервере.
     */
    private static void registration(){
        if (getServerAddressr() == null)
            server();
        if (NetworkConnection.signUp())
            connect();
    }
}
