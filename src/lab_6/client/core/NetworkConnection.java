package lab_6.client.core;

import lab_6.message.Crypted;
import lab_6.message.Message;
import lab_6.message.loggingIn.*;
import lab_6.message.registration.*;
import lab_6.crypto.ObjectCryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.time.Instant;
import java.util.Arrays;

import static lab_6.Settings.*;

/**
 * Класс для реализации сетевой коммуникации на стороне клиента.
 */
public class NetworkConnection {
    /**
     * Отправить команду на сервер и получить ответ на неё.
     * @param message Команда
     * @return Ответ
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws ClassNotFoundException
     */
    public static Message command(Message message) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException {
        Object response = objectSend(objectCryption.messageEncrypt(message));
        return objectCryption.messageDecrypt((Crypted)response);
    }
    /**
     * Настроить сетевое соединение: Установить Адрес сервера и порт.
     * @param hostname Адрес сервера
     * @param port Порт
     */
    public static void setServerAddressr(String hostname, int port){
        serverAddress = new InetSocketAddress(hostname, port);
    }
    /**
     * @return Текущее соединение.
     */
    public static InetSocketAddress getServerAddressr(){
        return serverAddress;
    }
    /**
     * Процесс регистрации.
     * @return Успешность
     */
    public static boolean signUp() {
        try {
            String password;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.login = objectCryption.getUserLogin();
            if (objectCryption.getUserLogin().length() < loginMinimalLength || objectCryption.getUserLogin().length() > loginMaximalLength) {
                System.out.println("!!! Login must be %d to %d characters !!!");
                return false;
            }
            System.out.print("\nEnter your password: ");
            password = reader.readLine();
            System.out.print("\nRetype your password: ");
            String passwordRetype = reader.readLine();
            if (!passwordRetype.equals(password)) {
                System.out.println("Passwords do not match");
                return false;
            }
            if (password.equals("")){
                System.out.println(new StringBuffer()
                        .append("\n\"Enter\" instead of a password\n")
                        .append("Glory to Richard Matthew Stallman !!!\n")
                        .append("\"NO\" to passwords !!!\n\n"));
            }
            System.out.println("\nGenerating RSA pair...");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            RSAKeyGenParameterSpec kpgSpec = new RSAKeyGenParameterSpec(userRSAKeyLength, BigInteger.probablePrime(userRSAKeyLength - 1, new SecureRandom()));
            System.out.println("Generating done");
            System.out.println("Generating encrypted AES passwords");
            keyPairGenerator.initialize(kpgSpec);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            registrationRequest.publicKey = keyPair.getPublic().getEncoded();
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = Arrays.copyOf(sha.digest(password.getBytes(Charset.forName("UTF-8"))), userAESKeySize);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            registrationRequest.privateKey = cipher.doFinal(keyPair.getPrivate().getEncoded());
            System.out.println("Generating done");
            System.out.println("Waiting for registration from the server");
            RegistrationResponse registrationResponse = registration(registrationRequest);
            System.out.print("Registration: ");
            if (registrationResponse.confirm) {
                System.out.println(registrationResponse.message);
                objectCryption.setSecretKey(secretKey);
                return true;
            } else
                System.out.println("failed\nReason: " + registrationResponse.message);
            return false;
        }catch (UnresolvedAddressException ex){
            System.out.println("Address is incorrect");
            return false;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /**
     * Процесс авторизации.
     * @return Успешность
     */
    public static boolean signIn() {
        try {
            String password;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            IdentificationRequest identificationRequest = new IdentificationRequest();
            identificationRequest.login = NetworkConnection.objectCryption.getUserLogin();
            IdentificationResponse identificationResponse = identification(identificationRequest);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            System.out.print("Logging in...\nEnter your password: ");
            password = reader.readLine();
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(sha.digest(password.getBytes(Charset.forName("UTF-8"))), userAESKeySize), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] privateKey;
            System.out.println(identificationResponse.message);
            try {
                privateKey = cipher.doFinal(identificationResponse.privateKey);
            } catch (Exception ex) {
                System.out.println("\nPassword incorrect");
                return false;
            }
            Cipher cipher2 = Cipher.getInstance("RSA");
            cipher2.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey)));
            try {
                authenticationRequest.random = cipher2.doFinal(identificationResponse.random);
            } catch (Exception e) {
                System.out.println("\nPassword incorrect");
                return false;
            }
            authenticationRequest.login = NetworkConnection.objectCryption.getUserLogin();
            AuthenticationResponse authenticationResponse = authentication(authenticationRequest);
            if (authenticationResponse.message.equals("success")) {
                byte[] secretKey;
                try {
                    secretKey = cipher2.doFinal(authenticationResponse.secretKey);
                } catch (Exception e) {
                    System.out.println("\nPassword incorrect");
                    return false;
                }
                objectCryption.setSecretKey(secretKey);
                return true;
            }
            System.out.println("Authentication failed: " + authenticationResponse.message);
            return false;
        } catch (UnresolvedAddressException ex){
            System.out.println("Address is incorrect");
            return false;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /**
     * Отправить Object на сервер и получить Object в ответ.
     * @param message отправляемый Object
     * @return получаемый Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Object objectSend(Object message) throws IOException, ClassNotFoundException {
        SocketChannel server = SocketChannel.open(serverAddress);
        ByteBuffer outBuffer = ByteBuffer.wrap(objectCryption.messageSerialize(message));
        server.write(outBuffer);
        outBuffer.clear();
        ByteBuffer byteBuffer = ByteBuffer.allocate(clientReceiveBuffer);
        long time = Instant.now().getEpochSecond();
        while (server.read(byteBuffer) != -1 && (Instant.now().getEpochSecond() - time < clientReceiveTimeout)){ }
        Object response = objectCryption.messageDeserialize(byteBuffer.array());
        server.close();
        return response;
    }
    /**
     * Процесс идентификации пользователя.
     * @return Ответ сервера
     */
    private static IdentificationResponse identification (IdentificationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (IdentificationResponse)response;
    }
    /**
     * Процесс регистрации пользователя.
     * @return Ответ сервера
     */
    private static RegistrationResponse registration (RegistrationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (RegistrationResponse)response;
    }
    /**
     * Процесс аутентификации пользователя.
     * @return Ответ сервера
     */
    private static AuthenticationResponse authentication (AuthenticationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (AuthenticationResponse)response;
    }
    /**
     * Текущее сетевое соединение.
     */
    private static InetSocketAddress serverAddress;
    /**
     * Экземпляр класса ObjectCryption для работы с шифрованием и сериализацией.
     */
    public static ObjectCryption objectCryption = new ObjectCryption();
}
