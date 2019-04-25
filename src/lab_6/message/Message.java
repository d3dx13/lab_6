package lab_6.message;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

/**
 * Класс, экземпляры которого передают элементы и команды между клиентом и сервером.
 */
public final class Message implements Serializable {
    public String login;
    public String text;
    public long time;
    public LinkedList<Object>values = new LinkedList<Object>();
}
