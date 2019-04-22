package lab_6.message;

import java.io.Serializable;

public final class Account implements Serializable {
    public String login;
    public byte [] publicKey;
    public byte [] privateKey; //Зашифрованный AES по паролю пользователя
    public String registrationDate;
    public byte [] random = null; //Сессионная случайная последовательность !!! Не сохранять в базе данных !!!
    public byte [] secretKey = null; //Сессионный пароль AES-256 !!! Не сохранять в базе данных !!!
}
