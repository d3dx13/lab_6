package lab_6;

import lab_6.message.loggingIn.*;
import lab_6.message.registration.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
        try {
            RegistrationRequest reg = new RegistrationRequest();

            String LOGIN = "d3dx13";
            String PASSWD = "12345678";

            reg.login = LOGIN;

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            RSAKeyGenParameterSpec kpgSpec = new RSAKeyGenParameterSpec(3072, BigInteger.probablePrime(3072-1, new SecureRandom()));
            keyPairGenerator.initialize(kpgSpec);

            KeyPair keyPair = keyPairGenerator.genKeyPair();
            reg.publicKey = keyPair.getPublic().getEncoded();

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(sha.digest(PASSWD.getBytes(Charset.forName("UTF-8"))), 32), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            reg.privateKey = cipher.doFinal(keyPair.getPrivate().getEncoded());

            System.out.println(reg.login);
            System.out.println("\n===\n");
            System.out.println(new String(reg.publicKey, Charset.forName("UTF-8")));
            System.out.println("\n===\n");
            System.out.println(new String(reg.privateKey, Charset.forName("UTF-8")));
            System.out.println("\n\n\nNew user added\n\n");


            IdentificationRequest log = new IdentificationRequest();
            log.login = LOGIN;


            System.out.println("\n\n\nloggingIn founded\n\n");


            IdentificationResponse log_res = new IdentificationResponse();


            Cipher cipher_2 = Cipher.getInstance("RSA");
            cipher_2.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(reg.publicKey)));

            Random Random_2 = new Random();
            byte [] temp = new byte[373];
            Random_2.nextBytes(temp);
            log_res.random = cipher_2.doFinal(temp);
            log_res.privateKey = reg.privateKey.clone();


            System.out.println(new String(log_res.privateKey));
            System.out.println("\n===\n");
            System.out.println(new String(log_res.random));
            System.out.println("\n===\n");


            System.out.println("\n\n\nloggingIn Response received\n\n");

            MessageDigest sha_3 = MessageDigest.getInstance("SHA-256");
            SecretKeySpec secretKeySpec_3 = new SecretKeySpec(Arrays.copyOf(sha_3.digest(PASSWD.getBytes(Charset.forName("UTF-8"))), 32), "AES");
            Cipher cipher_3 = Cipher.getInstance("AES");
            cipher_3.init(Cipher.DECRYPT_MODE, secretKeySpec_3);

            byte[] privateKey_3 = cipher_3.doFinal(log_res.privateKey);

            cipher_3 = Cipher.getInstance("RSA");
            cipher_3.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey_3)));

            byte[] random = cipher_3.doFinal(log_res.random);


            System.out.println(new String(random));
            System.out.println("\n===\n");


            System.out.println("\n\n\nServer recv random\n\n\n");
            System.out.println("Одинаковые ли случайные последовательности?"+" :: "+Arrays.equals(temp, random));


        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
