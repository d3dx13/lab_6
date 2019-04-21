package lab_6.message.registration;
import java.io.Serializable;

public final class RegistrationRequest implements Serializable {
    public String login;
    public byte [] publicKey;
    public byte [] privateKey; //Зашифрованный AES по паролю пользователя
}
