package lab_6.message;
import java.io.Serializable;
import java.util.LinkedList;

public final class Message implements Serializable {
    public String login;
    public String text;
    public long time;
    public LinkedList<Object>values = new LinkedList<Object>();
}
