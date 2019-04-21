package lab_6.message;

import java.io.Serializable;
import java.util.Date;

public final class Account implements Serializable {
    public String login;
    public byte [] publicKey;
    public byte [] privateKey; //Зашифрованный AES по паролю пользователя
    public String registrationDate;
}
