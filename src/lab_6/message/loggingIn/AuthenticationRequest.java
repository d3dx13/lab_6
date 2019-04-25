package lab_6.message.loggingIn;
import java.io.Serializable;

/**
 * Запрос пользователя на аутентификацию.
 */
public final class AuthenticationRequest implements Serializable {
    public String login;
    public byte [] random;
}
