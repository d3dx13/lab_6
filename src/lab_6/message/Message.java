package lab_6.message;
import java.io.Serializable;
import java.util.LinkedList;

public class Message implements Serializable {
    String text;
    LinkedList<String>keys;
    LinkedList<Object>values;
}
