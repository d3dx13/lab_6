package lab_6.message.loggingIn;
import java.io.Serializable;

public final class IdentificationResponse implements Serializable {
    public byte [] random;
    public byte [] privateKey;
    public String message;
}
