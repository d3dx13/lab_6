package lab_6.message.loggingIn;
import java.io.Serializable;

/**
 * Запрос пользователя на идентификацию.
 */
public final class IdentificationRequest implements Serializable {
    public String login;
}
