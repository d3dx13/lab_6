package lab_6.message.loggingIn;
import java.io.Serializable;

public final class AuthenticationRequest implements Serializable {
    public String login;
    public byte [] random;
}
