package lab_6.world.creation;

import java.io.*;
import java.util.*;

import lab_6.world.exception.SetupHyperParametersException;
import lab_6.world.state.DynamicsState;
import lab_6.world.state.FeelState;
import lab_6.world.state.PositionState;
import lab_6.world.state.ThinkState;
import org.json.*;

public class Square implements Serializable{
    public PriorityQueue<Dancer> container = new PriorityQueue<Dancer>(30);
    public Date date = new Date();
    public String type = "Empty";
    public String initTime = "Empty";
    public String lastChangeTime = "Empty";

    public Square(){
        class MyShutdownHook extends Thread {
            public void run() {
                save();
                info();
            }
        }
        MyShutdownHook shutdownHook = new MyShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
    /**<p>Преобразует JSON в Dancer
     * Возможные параметры:
     * String : name - имя
     * int : danceQuality - начальное умение танцевать
     * enum DynamicsState : dynamics - начальное динамическое состояние
     * enum feelState : feel - начальные чувства
     * enum thinkState : think - начальное мысли
     * enum positionState : position - начальное положение в пространстве</p>
     * @param message JSON Объект
     * @return Dancer : Объект или null, если JSON некорректен
     */
    public Dancer convertFromJSON(String message) {
        JSONObject obj;
        try {
            obj = new JSONObject(message);
        } catch (Exception ex){
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("[ERROR, БУНД!!!]");
            System.out.println("Вот как можно взять и неверно ввести JSON?");
            System.out.println("Почитайте: https://medium.com/@stasonmars/%D0%B2%D0%B2%D0%B5%D0%B4%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B2-json-c798d2723107");
            System.out.println("...");
            System.out.println("Ладно, все мы ленивые...");
            System.out.println("JSON начинается на { и заканчивается на }");
            System.out.println("Внутри этих скобочек через запятую (',' - вот этот знак)");
            System.out.println("Пишутся ваши команды. И, нет, ковычки - не часть знака.");
            System.out.println("Вид команды таков:");
            System.out.println("\"[ИМЯ ПОЛЯ]\" : \"[ЗНАЧЕНИЕ ПОЛЯ]\"");
            System.out.println("Обратите внимание на двойные кавычки, и на то, что имя и значение отделены двоеточием.");
            System.out.println("Но конкретно эта программа толекантна к отсутствию СРАЗУ ДВУХ кавычек у имени поля или его значения.");
            System.out.println("Итак, данного инструментала хватит для работы с данной программой. но если вам не лень потратить 15 минут,");
            System.out.println("Советую прочитать статью и понять, как много можно в JSON.");
            System.out.println("...");
            System.out.println("Вот пример для тех, кто сразу смотрит вниз собщения:");
            System.out.println("{\"name\" : \"Ricardo Milos\", \"dynamics\" : \"DANCING\"}");
            System.out.println();
            return null;
        }
        Map<String, Object>dataMap = obj.toMap();
        Dancer tempDancer = new Dancer("NoName");
        tempDancer.dynamicsStateState = DynamicsState.NEUTRAL;
        tempDancer.feelState = FeelState.NEUTRAL;
        tempDancer.thinkState = ThinkState.NEUTRAL;
        tempDancer.positionState = PositionState.NEUTRAL;
        for (Map.Entry<String, Object> iter : dataMap.entrySet()){
            if (iter.getKey().indexOf('<') != -1 || iter.getKey().indexOf('>') != -1){
                System.out.println();
                System.out.println("[ERROR]");
                System.out.println("Вы ввели имя поля: " + iter.getKey());
                System.out.println("Так как ваш заказчик выбрал для хранения базы данных XML, вы не можете использовать:");
                System.out.println("Символы '<' и '>' в имени и значенииполя");
                System.out.println("Поле не будет создано");
                System.out.println("А заказчику передайте, чтобы думал об MySQL");
                System.out.println();
                continue;
            }
            if (iter.getValue().toString().indexOf('>') != -1 || iter.getValue().toString().indexOf('<') != -1){
                System.out.println();
                System.out.println("[ERROR]");
                System.out.println("Вы ввели значение поля: " + iter.getValue().toString());
                System.out.println("Так как ваш заказчик выбрал для хранения базы данных XML, вы не можете использовать:");
                System.out.println("Символы '<' и '>' в имени и значении поля");
                System.out.println("Поле не будет создано");
                System.out.println("А заказчику передайте, чтобы думал об MySQL");
                System.out.println();
                continue;
            }
            if (! tempDancer.setParam(iter.getKey(), iter.getValue().toString())){
                System.out.println();
                System.out.println("[ERROR]");
                System.out.println("Объект Dancer " + tempDancer + " не имеет поля " + iter.getKey() + ", принимающего значение " + iter.getValue().toString());
                System.out.println("Поле не будет создано");
                System.out.println();
            }
        }
        return tempDancer;
    }

    /**<p>Добавить новый элемент в коллекцию</p>
     * @param element Dancer : элемент
     * @return boolean : Успех выполнения
     */
    public boolean add (Dancer element){
        if (element == null)
            return false;
        return container.add(element);
    }

    /**<p>Вывести в стандартный поток вывода все элементы коллекции в строковом представлении</p>
     */
    public void show(){
        System.out.println("--- Square ---");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("    name    |dance points|  dynamics  |    feel    |   think    |  position  |");
        System.out.println("------------------------------------------------------------------------------");
        PriorityQueue<Dancer> tempContainer = new PriorityQueue<Dancer>(30);
        tempContainer.addAll(container);
        while (!tempContainer.isEmpty()){
            Dancer iter = tempContainer.poll();
            System.out.printf("%12s|%12d|%12s|%12s|%12s|%12s|\n", iter.toString(), iter.danceQuality, iter.dynamicsStateState, iter.feelState, iter.thinkState, iter.positionState.toString());
        }
        System.out.println("------------------------------------------------------------------------------");
        System.out.println();
    }

    /**<p>Сохранить коллекцию в файл</p>
     * @return boolean : Успех выполнения
     */
    public boolean save(){
        try {
            File temp = new File(System.getenv().get("COLLECTION_PATH"));
            temp.delete();
            Writer bufferedOutputStream = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(System.getenv().get("COLLECTION_PATH"))));
            bufferedOutputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bufferedOutputStream.write("<Square>\n");
            bufferedOutputStream.write("    <info>\n");
            bufferedOutputStream.write("        <type>\n");
            bufferedOutputStream.write("            " + this.type + "\n");
            bufferedOutputStream.write("        </type>\n");
            bufferedOutputStream.write("        <initTime>\n");
            bufferedOutputStream.write("            " + this.initTime + "\n");
            bufferedOutputStream.write("        </initTime>\n");
            bufferedOutputStream.write("        <lastChangeTime>\n");
            bufferedOutputStream.write("            " + this.lastChangeTime + "\n");
            bufferedOutputStream.write("        </lastChangeTime>\n");
            bufferedOutputStream.write("    </info>\n");

            for (Dancer iter : container){
                bufferedOutputStream.write("    <Dancer>\n");
                if (! iter.name.equals("NoName")){
                    bufferedOutputStream.write("        <name>\n");
                    bufferedOutputStream.write("            " + iter.name + "\n");
                    bufferedOutputStream.write("        </name>\n");
                }
                bufferedOutputStream.write("        <danceQuality>\n");
                bufferedOutputStream.write("            " + iter.danceQuality + "\n");
                bufferedOutputStream.write("        </danceQuality>\n");
                if (iter.dynamicsStateState != null){
                    bufferedOutputStream.write("        <dynamics>\n");
                    bufferedOutputStream.write("            " + iter.dynamicsStateState.name() + "\n");
                    bufferedOutputStream.write("        </dynamics>\n");
                }
                if (iter.feelState != null){
                    bufferedOutputStream.write("        <feel>\n");
                    bufferedOutputStream.write("            " + iter.feelState.name() + "\n");
                    bufferedOutputStream.write("        </feel>\n");
                }
                if (iter.thinkState != null){
                    bufferedOutputStream.write("        <think>\n");
                    bufferedOutputStream.write("            " + iter.thinkState.name() + "\n");
                    bufferedOutputStream.write("        </think>\n");
                }
                if (iter.positionState != null){
                    bufferedOutputStream.write("        <position>\n");
                    bufferedOutputStream.write("            " + iter.positionState.name() + "\n");
                    bufferedOutputStream.write("        </position>\n");
                }
                bufferedOutputStream.write("    </Dancer>\n");
            }
            bufferedOutputStream.write("</Square>\n");
            bufferedOutputStream.close();
            this.lastChangeTime = this.date.toString();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**<p>Загрузить коллекцию из файла</p>
     * @return boolean : Успех выполнения
     */
    public boolean load(){
        PriorityQueue<Dancer>tempContainer = new PriorityQueue<Dancer>(30);
        tempContainer.addAll(container);
        container.clear();
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            Dancer tempDancer = new Dancer("NoName");
            Stack<String> nameTrace = new Stack<String>();
            if (!System.getenv().containsKey("COLLECTION_PATH"))
                throw new Exception() {
                    @Override
                    public String getMessage() {
                        return "Пожалуйста, задайте переменную окружения: \"COLLECTION_PATH\"\nВ данный момент программа не видит её\nЭто путь к вашей коллекции на диске";
                    }
                };
            File ff = new File(System.getenv().get("COLLECTION_PATH"));
            ff.setReadable(true);
            InputStream inputStream = new FileInputStream(ff);
            Reader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            String message = "";
            while (data != -1) {
                if (data == '<') {
                    data = inputStreamReader.read();
                    String sectionName = "";
                    if (data == '?') {
                        while (data != '>')
                            data = inputStreamReader.read();
                    } else if (data == '/') {
                        message = message.trim();
                        if (!message.equals("")) {
                            if (nameTrace.toArray().length >= 3 && nameTrace.toArray()[0].equals("Square") && nameTrace.toArray()[1].equals("Dancer")) {
                                tempDancer.setParam((String) nameTrace.toArray()[2], message);
                            } else if (nameTrace.toArray().length >= 3 && nameTrace.toArray()[0].equals("Square") && nameTrace.toArray()[1].equals("info")) {
                                switch ((String) nameTrace.toArray()[2]) {
                                    case "initTime":
                                        this.initTime = message;
                                        break;
                                    case "lastChangeTime":
                                        this.lastChangeTime = message;
                                        break;
                                    case "type":
                                        this.type = message;
                                        break;
                                }
                            }
                        }
                        sectionName = "";
                        data = inputStreamReader.read();
                        while (data != '>') {
                            sectionName += (char) data;
                            data = inputStreamReader.read();
                        }
                        if (!nameTrace.empty()) {
                            if (nameTrace.peek().equals(sectionName)) {
                                if (sectionName.equals("Dancer")) {
                                    this.add(tempDancer);
                                }
                                nameTrace.pop();
                            } else {
                                throw new SetupHyperParametersException();
                            }
                        }
                    } else {
                        while (data != '>') {
                            sectionName += (char) data;
                            data = inputStreamReader.read();
                        }
                        nameTrace.push(sectionName);
                        if (sectionName.equals("Dancer"))
                            tempDancer = new Dancer("NoName");
                    }
                    data = inputStreamReader.read();
                    message = "";
                }
                message += (char) data;
                data = inputStreamReader.read();
            }
            inputStreamReader.close();
            if (this.type.equals("Empty") ||
                    this.initTime.equals("Empty") ||
                    this.lastChangeTime.equals("Empty")
            ) {
                System.out.println("\nКоллекция повреждена или некорректна.\nИнформационное опознавательное поле <info> некорректно\nЖелаете восстановить из файла коллекцию, насколько это возможно?");
                System.out.println("\'y\' или \'Y\' для подтверждения.");
                String msg = scanner.nextLine();
                if (msg.indexOf(121) == -1 && msg.indexOf(89) == -1)
                    return false;
                this.type = "Dancer";
                this.initTime = date.toString();
                this.lastChangeTime = date.toString();
                return true;
            }
            return true;
        } catch (FileNotFoundException ex){
            container.addAll(tempContainer);
            System.out.println("Пожалуйста, правильно задайте переменную окружения: \"COLLECTION_PATH\"");
            System.out.println("Переменная окружения должна ссылаться к доступному файлу");
            System.out.println(ex.getMessage());
            System.out.println("\nЖелаете пересоздать эту коллекцию по этому пути?");
            System.out.println("\'y\' или \'Y\' для подтверждения.");
            try {
                String msg = scanner.nextLine();
                if (msg.indexOf(121) > -1 || msg.indexOf(89) > -1){
                    File temp = new File(System.getenv().get("COLLECTION_PATH"));
                    temp.delete();
                    if (temp.createNewFile()){
                        System.out.println("Файл создан");
                        this.type = "Dancer";
                        this.initTime = date.toString();
                        this.lastChangeTime = date.toString();
                        return this.save();
                    }
                    else
                        System.out.println("Файл не может быть создан");
                }
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
            return false;
        } catch (SetupHyperParametersException ex){
            container.addAll(tempContainer);
            System.out.println("XML база данных имеет неверный синтаксис.");
            System.out.println("Курить: https://www.ibm.com/developerworks/ru/library/x-newxml/index.html");
            System.out.println("\nЖелаете уничтожить эту коллекцию и создать новую на её месте?");
            System.out.println("\'y\' или \'Y\' для подтверждения.");
            try {
                String msg = scanner.nextLine();
                if (msg.indexOf(121) > -1 || msg.indexOf(89) > -1){
                    File temp = new File(System.getenv().get("COLLECTION_PATH"));
                    temp.delete();
                    if (temp.createNewFile()){
                        System.out.println("Файл создан");
                        this.type = "Dancer";
                        this.initTime = date.toString();
                        this.lastChangeTime = date.toString();
                        return this.save();
                    }
                    else
                        System.out.println("Файл не может быть создан");
                }
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
            return false;
        } catch (Exception ex) {
            container.addAll(tempContainer);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**<p>Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)</p>
     */
    public void info(){
        System.out.println();
        System.out.println("--- Square info ---");
        System.out.println("Тип коллекции:             " + this.type);
        System.out.println("Дата инициализации:        " + this.initTime);
        System.out.println("Дата последнего изменения: " + this.lastChangeTime);
        System.out.println("Количество элементов:      " + this.container.size());
        System.out.println("-------------------");
        System.out.println();
    }

    /**<p>Вывести в стандартный поток вывода помощь по командам</p>
     */
    public void help(){
        StringBuilder row[] = {new StringBuilder("=== "), new StringBuilder("====== "), new StringBuilder("====================")};

        StringBuilder sb = new StringBuilder();
        sb
            .append("--- Commands ---")
            .append("help - ")
            .append("");

        System.out.println(sb.toString());

        System.out.println("\n--- Commands ---");
        System.out.println("help - " + "Вывести в стандартный поток вывода помощь по командам");
        System.out.println("exit - " + "Закрыть программу");
        System.out.println("add {} - " + "Добавить новый элемент в коллекцию");
        System.out.println("remove {} - " + "Удалить элемент из коллекции по его значению");
        System.out.println("add_if_min {} - " + "Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        System.out.println("add_if_max {} - " + "Добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        System.out.println("show || ls- " + "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        System.out.println("save - " + "Сохранить коллекцию в файл");
        System.out.println("load - " + "Загрузить коллекцию в файл");
        System.out.println("info - " + "Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, дата последнего изменения, количество элементов)");
        System.out.println("\n\n--- JSON params ---");
        System.out.println("--- [ЗНАЧЕНИЕ] : [описание] ---");
        System.out.println("-------------------");
        System.out.println(row[0] + "name " + row[0]);
        System.out.println(row[1] + "String : Строка с именем");
        System.out.println(row[2]);
        System.out.println(row[0] + "danceQuality " + row[0]);
        System.out.println(row[1] + "int : Начальное количество \"dance points\"");
        System.out.println(row[2]);
        System.out.println(row[0] + "dynamics " +row[0]);
        for (PositionState iter : PositionState.values())
            System.out.printf("%s%s : %s\n", row[1], iter.name(), iter.toString());
        System.out.println(row[2]);
        System.out.println(row[0] + "feel " +row[0]);
        for (FeelState iter : FeelState.values())
            System.out.printf("%s%s : %s\n", row[1], iter.name(), iter.toString());
        System.out.println(row[2]);
        System.out.println(row[0] + "think " +row[0]);
        for (ThinkState iter : ThinkState.values())
            System.out.printf("%s%s : %s\n", row[1], iter.name(), iter.toString());
        System.out.println(row[2]);
        System.out.println(row[0] + "position " +row[0]);
        for (PositionState iter : PositionState.values())
            System.out.printf("%s%s : %s\n", row[1], iter.name(), iter.toString());
        System.out.println(row[2]);
    }

    /**<p>Удалить элемент из коллекции по его значению</p>
     * @param element People : элемент
     * @return boolean : Успех выполнения
     */
    public boolean remove(Dancer element){
        if (element == null)
            return false;
        for (Dancer iter : container) {
            if (iter.equals(element)) {
                return container.remove(iter);
            }
        }
        return false;
    }

    /**<p>Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции</p>
     * @param element Dancer : элемент
     * @return boolean : Успех выполнения
     */
    public boolean add_if_min(Dancer element){
        if (element == null)
            return false;
        for (Dancer iter : container){
            if (element.danceQuality > iter.danceQuality)
                return false;
        }
        return container.add(element);
    }

    /**<p>Добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции</p>
     * @param element Dancer : элемент
     * @return boolean : Успех выполнения
     */
    public boolean add_if_max(Dancer element){
        if (element == null)
            return false;
        for (Dancer iter : container){
            if (element.danceQuality < iter.danceQuality)
                return false;
        }
        return container.add(element);
    }

}
