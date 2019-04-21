package lab_6.message;
import java.io.Serializable;
import java.sql.Time;
import java.util.LinkedList;

public final class Message implements Serializable {
    public String login;
    public long time;
    public String text;
    public boolean isCommandShow;
    public LinkedList<String>keys;
    public LinkedList<Object>values;
}
