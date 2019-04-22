package lab_6.client.core;

import lab_6.message.Message;
import lab_6.message.loggingIn.AuthenticationRequest;
import lab_6.message.loggingIn.AuthenticationResponse;
import lab_6.message.loggingIn.IdentificationRequest;
import lab_6.message.loggingIn.IdentificationResponse;
import lab_6.message.registration.RegistrationRequest;
import lab_6.message.registration.RegistrationResponse;

public class NetworkConnection {
    public Message command(Message message){

        return new Message();
    }
    public Message connect(){

        return new Message();
    }
    public Message disconnect(){

        return new Message();
    }


    private IdentificationResponse identification (IdentificationRequest request){

        return new IdentificationResponse();
    }
    private RegistrationResponse registration (RegistrationRequest request){

        return new RegistrationResponse();
    }
    private AuthenticationResponse authentication (AuthenticationRequest request){

        return new AuthenticationResponse();
    }
    private String login;
    private byte [] secretKey;
}
