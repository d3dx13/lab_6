package lab_6.message.loggingIn;
import java.io.Serializable;

public class AuthenticationResponse implements Serializable {
    byte [] secretKey; //Сессионный AES ключ, зашифрованный открытым ключём RSA пользователя
}
