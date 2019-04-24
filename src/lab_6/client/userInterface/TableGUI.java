package lab_6.client.userInterface;

import lab_6.message.Message;
import lab_6.world.creation.Dancer;

import java.util.PriorityQueue;

public class TableGUI {
    public static void printTable(Message message){
        if (message.values == null)
            return;
        if (message.values.size() == 0){
            System.out.print("\n--- Collection is Empty ---\n");
            //return;
        }
        int[] tableMaxLength = new int[] {0, 0, 0, 0, 0, 0, 0};
        for (Object iter : message.values){
            Dancer dancer = (Dancer)iter;
            if (dancer.name.length() > tableMaxLength[0])
                tableMaxLength[0] = dancer.name.length();
            if (dancer.birthday.toString().length() > tableMaxLength[1])
                tableMaxLength[1] = dancer.birthday.toString().length();
            if (dancer.dynamicsStateState.toString().length() > tableMaxLength[2])
                tableMaxLength[2] = dancer.dynamicsStateState.toString().length();
        }
        System.out.printf("\n|%-"+20+"s|\n", "name wad");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("\n\n--- Collection ---");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("|    name    | birthday |  dynamics  |    feel    |   think    |  position  | dance points |");
        System.out.println("--------------------------------------------------------------------------------------------");
        //System.out.printf("%12s|%12d|%12s|%12s|%12s|%12s|\n", iter.toString(), iter.birthday, iter.danceQuality, iter.dynamicsStateState, iter.feelState, iter.thinkState, iter.positionState.toString());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println();


        System.out.println(message.values);
        if (message.values != null)
            message.values.forEach(o -> System.out.println("> " + ((Dancer) o).getDanceQuality() + " - " + ((Dancer) o)));
        System.out.println(message.text);
    }

    public static void main(String[] args) {
        printTable(new Message());
    }
}
