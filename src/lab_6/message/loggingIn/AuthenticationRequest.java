package lab_6.message.loggingIn;
import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
    byte [] secretKey; //Зашифрованный открытым ключём RSA пользователя
}
