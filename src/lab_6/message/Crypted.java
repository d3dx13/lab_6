package lab_6.message;
import java.io.Serializable;

/**
 * Пересылаемый по сети класс, содержащий зашифрованное сериализованное сообщение Message.
 */
public class Crypted implements Serializable {
    public byte[] data;
    public String login;
}
