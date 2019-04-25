package lab_6.server.core;

import lab_6.message.Account;
import lab_6.message.loggingIn.AuthenticationRequest;
import lab_6.message.loggingIn.AuthenticationResponse;
import lab_6.message.loggingIn.IdentificationRequest;
import lab_6.message.loggingIn.IdentificationResponse;
import lab_6.message.registration.RegistrationRequest;
import lab_6.message.registration.RegistrationResponse;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;

import static lab_6.Settings.*;
import static lab_6.server.Database.accounts;
import static lab_6.server.Database.accountsSave;

/**
 * Класс для обработки запросов пользователя на регистрацию, идентификацию и аутентификацию.
 */
class SignUpInHandler {
    /**
     * Метод обработки запроса на регистрацию от пользователя.
     * @param request Запрос регистрации.
     * @return Ответ на запрос регистрации.
     */
    static RegistrationResponse registration(RegistrationRequest request){
        RegistrationResponse response = new RegistrationResponse();
        if (request.login.length() < loginMinimalLength){
            response.confirm = false;
            response.message = "login is short";
            return response;
        }
        if (request.login.length() > loginMaximalLength){
            response.confirm = false;
            response.message = "login is long";
            return response;
        }
        Account tempAccount = new Account();
        tempAccount.login = request.login;
        tempAccount.publicKey = request.publicKey.clone();
        tempAccount.privateKey = request.privateKey.clone();
        tempAccount.registrationDate = (new Date()).toString();
        if (!accounts.containsKey(request.login)){
            accounts.putIfAbsent(request.login, tempAccount);
            accountsSave();
            response.confirm = true;
            response.message = "success";
        } else {
            response.confirm = false;
            response.message = "user exists";
        }
        return response;
    }
    /**
     * Метод обработки запроса на идентификацию от пользователя.
     * @param request Запрос идентификации.
     * @return Ответ на запрос идентификации.
     */
    static IdentificationResponse identification(IdentificationRequest request) {
        IdentificationResponse response = new IdentificationResponse();
        try {
            if (request.login.length() < loginMinimalLength) {
                response.message = "login is short";
                return response;
            }
            if (request.login.length() > loginMaximalLength) {
                response.message = "login is long";
                return response;
            }
            if (!accounts.containsKey(request.login)) {
                response.message = "login is wrong";
                return response;
            }
            Account user = accounts.get(request.login);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(user.publicKey)));
            SecureRandom secureRandom = new SecureRandom();
            user.random = new byte[identificationRandomSize];
            secureRandom.nextBytes(user.random);
            response.random = cipher.doFinal(user.random);
            response.privateKey = user.privateKey.clone();
            accounts.put(request.login, user);
            response.message = "success";
            return response;
        } catch (Exception ex){
            response.message = ex.getMessage();
            return response;
        }
    }
    /**
     * Метод обработки запроса на аутентификацию от пользователя.
     * @param request Запрос аутентификации.
     * @return Ответ на запрос аутентификации.
     */
    static AuthenticationResponse authentication(AuthenticationRequest request){
        AuthenticationResponse response = new AuthenticationResponse();
        if (request.login.length() < loginMinimalLength) {
            response.message = "login is short";
            return response;
        }
        if (request.login.length() > loginMaximalLength) {
            response.message = "login is long";
            return response;
        }
        if (!accounts.containsKey(request.login)) {
            response.message = "login is wrong";
            return response;
        }
        if (!(Arrays.equals(accounts.get(request.login).random, request.random))) {
            response.message = "random is wrong";
            return response;
        } else {
            try {
                Account user = accounts.get(request.login);
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(user.publicKey)));
                SecureRandom secureRandom = new SecureRandom();
                user.secretKey = new byte[userAESKeySize];
                secureRandom.nextBytes(user.secretKey);
                response.secretKey = cipher.doFinal(user.secretKey);
                accounts.put(request.login, user);
                response.message = "success";
                return response;
            } catch (Exception ex){
                response.message = ex.getMessage();
                return response;
            }
        }
    }
}
