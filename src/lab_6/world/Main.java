package lab_6.world;

import lab_6.world.creation.Dancer;
import lab_6.world.creation.Square;

import java.io.*;

public class Main {
    public static void main(String[] args){
        Square square = new Square();
        System.out.println("Загружаю коллекцию");
        if (!square.load())
            return;
        square.info();
        square.save();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit) {
            try {
                String command;
                System.out.print(">>> ");
                command = reader.readLine();
                if ((command.length() > 3 && command.substring(0,4).equals("show")) || command.equals("ls")){
                    square.show();
                }
                else if (command.length() > 3 && command.substring(0,4).equals("save")){
                    if (square.save())
                        System.out.println("success");
                    else
                        System.out.println("failure");
                }
                else if (command.length() > 3 && command.substring(0,4).equals("load")){
                    if (square.load())
                        System.out.println("success");
                    else
                        System.out.println("failure");
                }
                else if (command.length() > 3 && command.substring(0,4).equals("info")){
                    square.info();
                }
                else if (command.length() > 5 && command.substring(0,6).equals("remove")){
                    Dancer tempDancer = square.convertFromJSON(command.substring(6));
                    if (square.remove(tempDancer))
                        System.out.println("success");
                    else
                        System.out.println("failure");
                }
                else if (command.length() > 9 && command.substring(0,10).equals("add_if_min")){
                    System.out.println(command);
                    Dancer tempDancer = square.convertFromJSON(command.substring(10));
                    if (square.add_if_min(tempDancer))
                        System.out.println("success");
                    else
                        System.out.println("failure");
                }
                else if (command.length() > 9 && command.substring(0,10).equals("add_if_max")){
                    System.out.println(command);
                    Dancer tempDancer = square.convertFromJSON(command.substring(10));
                    if (square.add_if_max(tempDancer))
                        System.out.println("success");
                    else
                        System.out.println("failure");
                }
                else if (command.length() > 2 && command.substring(0,3).equals("add")){
                    Dancer tempDancer = square.convertFromJSON(command.substring(3));
                    if (square.add(tempDancer))
                        System.out.println("success");
                    else
                        System.out.println("failure");
                }
                else if (command.length() > 3 && command.substring(0,4).equals("exit")){
                    exit = true;
                }
                else if (command.length() > 3 && command.substring(0,4).equals("help")){
                    square.help();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                exit = true;
            }
        }
    }
}
