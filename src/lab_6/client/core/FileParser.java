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
                            dancerObject.setParam(dancerParametr.getNodeName(),dancerParametr.getChildNodes().item(0).getTextContent());
                        }
                    }
                    xmlFileMessage.values.add(dancerObject);
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

        return xmlFileMessage;
    }

}
