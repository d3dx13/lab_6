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

public class ObjectCryption {
    public void setUserLogin(String message){
        login = message;
    }
    public void setSecretKey(byte [] message){
        secretKey = message.clone();
    }
    public Crypted messageEncrypt(Message message) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Crypted response = new Crypted();
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
    public byte[] messageSerialize(Object message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(message);
        out.flush();
        return bos.toByteArray();
    }
    public Object messageDeserialize(byte[] message) throws IOException, ClassNotFoundException {
        ObjectInput out = new ObjectInputStream(new ByteArrayInputStream(message));
        return out.readObject();
    }
    public Message getNewMessage(){
        Message message = new Message();
        message.login = login;
        message.time = Instant.now().getEpochSecond() * 1000000L + (long) Instant.now().getNano() /1000;
        return message;
    }
    public Message getNewMessage(String text){
        Message message = getNewMessage();
        message.text = text;
        return message;
    }
    protected static String login;
    protected static byte [] secretKey;
}
