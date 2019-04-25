package lab_6.client.userInterface;

import lab_6.message.Message;
import lab_6.world.creation.Dancer;


/**
 * Класс для стандартизированных методов отрисовки графического интерфейса пользователя.
 */
class TableGUI {
    /**
     * Красивый вывод команды show в виде форматированной таблички.
     * @param message Сообщение, поле values которого будет выведено.
     */
    static void printTable(Message message){
        if (message.values == null)
            return;
        if (message.values.size() == 0){
            System.out.print("\n--- Collection is Empty ---\n");
            return;
        }
        String [] tableHeader  = new String [] {
                "name",
                "birthday",
                "dance points",
                "dynamics",
                "feel",
                "think",
                "position"
        };
        int[] tableMaxLength = new int[7];
        for (int i = 0; i < 7; i++)
            tableMaxLength[i] = tableHeader[i].length();
        for (Object iter : message.values){
            Dancer dancer = (Dancer)iter;
            if (dancer.name != null && dancer.name.length() > tableMaxLength[0])
                tableMaxLength[0] = dancer.name.length();
            if (dancer.birthday != null && dancer.birthday.toString().length() > tableMaxLength[1])
                tableMaxLength[1] = dancer.birthday.toString().length();
            if (String.valueOf(dancer.getDanceQuality()).length() > tableMaxLength[2])
                tableMaxLength[2] = String.valueOf(dancer.getDanceQuality()).length();
            if (dancer.dynamicsStateState != null && dancer.dynamicsStateState.toString().length() > tableMaxLength[3])
                tableMaxLength[3] = dancer.dynamicsStateState.toString().length();
            if (dancer.feelState != null && dancer.feelState.toString().length() > tableMaxLength[4])
                tableMaxLength[4] = dancer.feelState.toString().length();
            if (dancer.thinkState != null && dancer.thinkState.toString().length() > tableMaxLength[5])
                tableMaxLength[5] = dancer.thinkState.toString().length();
            if (dancer.positionState != null && dancer.positionState.toString().length() > tableMaxLength[6])
                tableMaxLength[6] = dancer.positionState.toString().length();
        }
        StringBuffer formatBuffer = new StringBuffer();
        for (int i = 0; i < 7; i++){
            formatBuffer.append("|%-");
            formatBuffer.append(tableMaxLength[i]);
            formatBuffer.append("s");
        }
        formatBuffer.append("|\n");
        String header = String.format(formatBuffer.toString(),
                tableHeader[0],
                tableHeader[1],
                tableHeader[2],
                tableHeader[3],
                tableHeader[4],
                tableHeader[5],
                tableHeader[6]);
        printLine('-', header.length() - 1);
        System.out.print(header);
        printLine('-', header.length() - 1);
        StringBuffer stringBuffer = new StringBuffer();
        for (Object iter : message.values){
            Dancer dancer = (Dancer)iter;
            stringBuffer.append(String.format(formatBuffer.toString(),
                    dancer.name,
                    dancer.birthday,
                    dancer.getDanceQuality(),
                    dancer.dynamicsStateState,
                    dancer.feelState,
                    dancer.thinkState,
                    dancer.positionState));
        }
        System.out.print(stringBuffer.toString());
        printLine('-', header.length() - 1);
    }
    /**
     * Вывести полосу из символов symbol длиной len.
     * @param symbol Символ
     * @param len Длина полосы
     */
    private static void printLine(char symbol, int len){
        for (int i = 0; i < len; i++)
            System.out.print(symbol);
        System.out.print('\n');
    }
}
