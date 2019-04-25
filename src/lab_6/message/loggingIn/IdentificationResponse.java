package lab_6.message.loggingIn;
import java.io.Serializable;

/**
 * Ответ на запрос пользователя на идентификацию.
 */
public final class IdentificationResponse implements Serializable {
    public byte [] random;
    public byte [] privateKey;
    public String message;
}
