package lab_6.client.core;

import lab_6.crypto.ObjectCryption;
import lab_6.message.Message;
import lab_6.world.creation.Dancer;

import java.io.*;
import java.util.*;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FileParser {

    public static Message getMessageFromXMLFile(String pathToFile)
    {


        Message xmlFileMessage = new ObjectCryption().getNewMessage();
        xmlFileMessage.text = "add";
        System.out.println(pathToFile);

        try {
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse(pathToFile);

            // Получаем корневой элемент
            Node root = document.getDocumentElement();

            // Просматриваем все подэлементы корневого - т.е. объекты
            NodeList dancers = root.getChildNodes();
            for (int i = 0; i < dancers.getLength(); i++) {
                Node dancer = dancers.item(i);
                // Если нода не текст, то это объект - заходим внутрь
                if (dancer.getNodeType() != Node.TEXT_NODE && dancer.getNodeName().equals("Dancer")) {
                    Dancer dancerObject = new Dancer("NoName");
                    NodeList dancerParametrs = dancer.getChildNodes();
                    for(int j = 0; j < dancerParametrs.getLength(); j++) {
                        Node dancerParametr = dancerParametrs.item(j);
                        // Если нода не текст, то это один из параметров объекта - печатаем
                        if (dancerParametr.getNodeType() != Node.TEXT_NODE) {
                            dancerObject.setParam(dancerParametr.getNodeName(),dancerParametr.getChildNodes().item(0).getTextContent());
                            //System.out.println(dancerParametr.getNodeName() + ":" + dancerParametr.getChildNodes().item(0).getTextContent());
                        }
                    }
                    xmlFileMessage.values.add(dancerObject);
                    //System.out.println("===========>>>>");
                }
            }

        } catch (FileNotFoundException fnfe){
            System.out.println("---Файл не найден. Его не существует или доступ к нему закрыт---");
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.out);
        } catch (SAXException ex) {
            ex.printStackTrace(System.out);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }


/*
        PriorityQueue<Dancer> tempContainer = new PriorityQueue<Dancer>(30);
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            Dancer tempDancer = new Dancer("NoName");
            Stack<String> nameTrace = new Stack<String>();
            if (!System.getenv().containsKey(pathToFile))
                throw new Exception() {
                    @Override
                    public String getMessage() {
                        return "Пожалуйста, задайте переменную окружения: \"COLLECTION_PATH\"\nВ данный момент программа не видит её\nЭто путь к вашей коллекции на диске";
                    }
                };
            File ff = new File(System.getenv().get(pathToFile));
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



*/
        return xmlFileMessage;
    }

}
