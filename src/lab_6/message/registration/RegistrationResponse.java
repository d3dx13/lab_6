package lab_6.message.registration;
import java.io.Serializable;

/**
 * Ответ на запрос пользователя на регистрацию.
 */
public final class RegistrationResponse implements Serializable {
    public boolean confirm;
    public String message;
}
