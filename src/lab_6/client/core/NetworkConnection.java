package lab_6.client.core;

import lab_6.message.Crypted;
import lab_6.message.Message;
import lab_6.message.loggingIn.AuthenticationRequest;
import lab_6.message.loggingIn.AuthenticationResponse;
import lab_6.message.loggingIn.IdentificationRequest;
import lab_6.message.loggingIn.IdentificationResponse;
import lab_6.message.registration.RegistrationRequest;
import lab_6.message.registration.RegistrationResponse;
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
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.time.Instant;
import java.util.Arrays;

import static java.time.Instant.now;
import static lab_6.Settings.loginMaximalLength;
import static lab_6.Settings.loginMinimalLength;
import static lab_6.Settings.userRSAkeyLength;

public class NetworkConnection {
    public static Message command(Message message) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException {
        Object response = objectSend(objectCryption.messageEncrypt(message));
        return objectCryption.messageDecrypt((Crypted)response);
    }

    public static void setServerAddressr(String hostname, int port){
        serverAddress = new InetSocketAddress(hostname, port);
    }

    public static boolean signUp() {
        try {
            String password;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.login = objectCryption.getUserLogin();
            if (objectCryption.getUserLogin().length() < loginMinimalLength || objectCryption.getUserLogin().length() > loginMaximalLength){
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
            System.out.println("\nGenerating RSA pair...");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            RSAKeyGenParameterSpec kpgSpec = new RSAKeyGenParameterSpec(userRSAkeyLength, BigInteger.probablePrime(userRSAkeyLength-1, new SecureRandom()));
            System.out.println("Generating done");
            System.out.println("Generating encrypted AES passwords");
            keyPairGenerator.initialize(kpgSpec);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            registrationRequest.publicKey = keyPair.getPublic().getEncoded();
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte [] secretKey = Arrays.copyOf(sha.digest(password.getBytes(Charset.forName("UTF-8"))), 32);
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
                System.out.println("failed\nReason: "+registrationResponse.message);
                return false;
        } catch (Exception ex){
            return false;
        }
    }


    public static boolean signIn() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, InvalidKeySpecException {
        String password;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        IdentificationRequest identificationRequest = new IdentificationRequest();
        identificationRequest.login = NetworkConnection.objectCryption.getUserLogin();
        IdentificationResponse identificationResponse = identification(identificationRequest);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        System.out.print("\nEnter your password: ");
        password = reader.readLine();
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(sha.digest(password.getBytes(Charset.forName("UTF-8"))), 32), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] privateKey;
        System.out.println(identificationResponse.message);
        try {
            privateKey = cipher.doFinal(identificationResponse.privateKey);
        }catch (Exception ex){
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
        if (authenticationResponse.message.equals("success")){
            byte [] secretKey;
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
    }
    public static void disconnect() throws IOException, ClassNotFoundException {
        objectSend(objectCryption.getNewMessage("disconnect"));
    }
    public static void status() throws IOException, ClassNotFoundException {
        objectSend(objectCryption.getNewMessage("status"));
    }

    protected static Object objectSend(Object message) throws IOException, ClassNotFoundException {
        server = SocketChannel.open(serverAddress);
        ByteBuffer outBuffer = ByteBuffer.wrap(objectCryption.messageSerialize(message));
        server.write(outBuffer);
        outBuffer.clear();
        byte [] buffer = server.socket().getInputStream().readAllBytes();
        Object response = objectCryption.messageDeserialize(buffer);
        server.close();
        return response;
    }
    protected static IdentificationResponse identification (IdentificationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (IdentificationResponse)response;
    }
    protected static RegistrationResponse registration (RegistrationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (RegistrationResponse)response;
    }
    protected static AuthenticationResponse authentication (AuthenticationRequest request) throws IOException, ClassNotFoundException {
        Object response = objectSend(request);
        return (AuthenticationResponse)response;
    }
    protected static SocketChannel server;
    protected static InetSocketAddress serverAddress;
    public static ObjectCryption objectCryption = new ObjectCryption();
}
