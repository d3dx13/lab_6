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

/**
 * Класс, содержащий метод для получения объектов из файла XML и возвращения их в коллекции объекта Message.
 */
public class FileParser {
    /**
     * Метод, возвращающий объект Message с коллекцией элементов, считанных из файла XML.
     * @param pathToFile Объект String, содержащий путь к файлу XML.
     * @return Объект Message, содержащий коллекцию элементов, считанных из файла XML.
     */
    public static Message getMessageFromXMLFile(String pathToFile)
    {
        Message xmlFileMessage = NetworkConnection.objectCryption.getNewMessage("add");

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
                            try {
                                dancerObject.setParam(dancerParametr.getNodeName(), dancerParametr.getChildNodes().item(0).getTextContent());
                            }catch (Exception e)
                            {
                                System.out.println("---Импорт файла не удался. Неправильно введён параметр одного из объектов в XML-файле." +
                                        " Может быть параметр съеден собакой?---");
                            }
                        }

                    }
                    xmlFileMessage.values.add(dancerObject);
                }
            }
        } catch (FileNotFoundException fnfe){
            System.out.println("---Файл не найден. Его не существует или доступ к нему закрыт.---\n---Предположительно, " +
                    "имперские штурмовики прорвались к хранилищу, но Вы можете помочь Даше путешественнице найти этот файл!---");
        } catch (ParserConfigurationException ex) {
            System.out.println("Это какая-то редкая, легендарная, никогда не появлявшаяся ошибка!\n" +
                    "Вы - первый, кто её обнаружил! Вам срочно нужно подойти к полке! Там Вас будет ждать пирожок."+ex);
        } catch (SAXException ex) {
            System.out.println("---Импорт XML-файла не удался. В нём существует ошибка! Это фиаско, братан...---");
        } catch (IOException ex) {
            System.out.println("Это какая-то редкая, легендарная, никогда не появлявшаяся ошибка!\n" +
                    "Вы - первый, кто её обнаружил! Вам срочно нужно подойти к полке! Там Вас будет ждать пирожок."+ex);
        }

        return xmlFileMessage;
    }

}
