package lab_6.crypto;

import lab_6.message.Crypted;
import lab_6.message.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;


/**
 * Служебный класс, реализующий работу с сериализацией и шифрованием объектов.
 */
public class ObjectCryption {
    /**
     * Установить логин пользователя, используемый для подписи сообщения.
     * @param message Логин
     */
    public void setUserLogin(String message){
        this.login = message;
    }
    /**
     * Получить логин пользователя, используемый для подписи сообщения.
     * @return Логин
     */
    public String getUserLogin(){
        return this.login;
    }
    /**
     * Установить секретный AES ключ пользователя.
     * @param message - ключ
     */
    public void setSecretKey(byte [] message){
        this.secretKey = message.clone();
    }
    /**
     * Зашифровать сообщение
     * @param message Сообщение
     * @return Зашифрованное сообщение
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public Crypted messageEncrypt(Message message) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Crypted response = new Crypted();
        response.login = this.login;
        SecureRandom secureRandom = new SecureRandom();
        response.data = messageSerialize(message);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte salt[] = response.data.clone();
        byte temp[] = response.data.clone();
        response.data = new byte[response.data.length*2];
        secureRandom.nextBytes(salt);
        for (int i = 0; i < salt.length; i++){
            response.data[i*2] = salt[i];
            response.data[i*2 + 1] = temp[i];
        }
        response.data = cipher.doFinal(response.data);
        return response;
    }
    /**
     * Расшифровать сообщение
     * @param message Зашифрованное сообщение
     * @return Расшифрованное сообщение
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public Message messageDecrypt(Crypted message) throws IOException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        message.data = cipher.doFinal(message.data);
        byte temp[] = message.data.clone();
        message.data = new byte[message.data.length/2];
        for (int i = 0; i < message.data.length; i++){
            message.data[i] = temp[i*2 + 1];
        }
        Message response = (Message)messageDeserialize(message.data);
        return response;
    }
    /**
     * Сериализовать Object
     * @param message Object для сериализации
     * @return Последовательность байт - сериализованный Object
     * @throws IOException
     */
    public byte[] messageSerialize(Object message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(message);
        out.flush();
        return bos.toByteArray();
    }
    /**
     * Десериализовать Object
     * @param message Последовательность байт - сериализованный Object
     * @return Object после десериализации.
     * @throws IOException
     */
    public Object messageDeserialize(byte[] message) throws IOException, ClassNotFoundException {
        ObjectInput out = new ObjectInputStream(new ByteArrayInputStream(message));
        return out.readObject();
    }
    /**
     * @return Message с заполненными полями login и time.
     */
    public Message getNewMessage(){
        Message message = new Message();
        message.login = login;
        message.time = Instant.now().getEpochSecond() * 1000000L + (long) Instant.now().getNano() /1000;
        return message;
    }
    /**
     * @param text Текст сообщения в Message.
     * @return Message с заполненными полями login, time и text.
     */
    public Message getNewMessage(String text){
        Message message = getNewMessage();
        message.text = text;
        return message;
    }
    /**
     * Логин пользователя.
     */
    protected String login;
    /**
     * Сессионный секретный ключ пользователя.
     */
    private byte [] secretKey;
}
