package lab_6.server.ThreadServer;

import lab_6.message.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class CommandHandler {
    public static void main(String[] args)
    {
        Message messageInThread = new Message();//приходит на вход функции
        Message answer = new Message();
        String command = messageInThread.text.trim();
        ConcurrentHashMap<String,Object> generalCollection = new ConcurrentHashMap<>();//приходит на вход функции

        String[] acceptedCommands = {"insert", "show", "remove_lower", "clear", "sort", "info", "remove", "exit"};


        String stringOfCommands="Введена неверная команда.\nСписок доступных команд:\n remove_lower {String key}\n " +
                "remove_lower {element}\n remove {String key}\n insert {String key} {element}\n show\n clear\n info\n sort\n exit";
        if(command.equals("")) {
            //сообщить о том, что введенной команды не существует
        } else {
            switch (command) {
                case "info":
                    answer.text = "Тип хранимой коллекции:"+generalCollection.getClass()
                            +".\nКоличество элементов в коллекции:"+generalCollection.size()+".";
                    break;
                case "show":
                    //commandShow(world);
                    break;
                case "clear":
                    generalCollection.clear();
                    answer.text = "Все элементы коллекции удалены.";
                    break;
                case "insert":


                    //commandInsert(consoleLine, world);
                    break;
                case "remove":
                    //commandRemove(consoleLine, world);
                    break;
                case "remove_lower":
                    //commandRemoveLower(consoleLine, world);
                    break;
                case "sort":
                    //commandSort(world);
                    break;
                case "exit":
                    //save(world);
                    break;
                default:
                    //System.out.println(stringOfCommands);
                    break;
            }

        }


    }


}
