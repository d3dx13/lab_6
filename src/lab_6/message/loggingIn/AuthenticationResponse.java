package lab_6.message.loggingIn;
import java.io.Serializable;

public final class AuthenticationResponse implements Serializable {
    public byte [] secretKey; //Сессионный AES ключ, зашифрованный открытым ключём RSA пользователя
    public String message;
}
