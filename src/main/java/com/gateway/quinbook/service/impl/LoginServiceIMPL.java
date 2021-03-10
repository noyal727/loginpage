package com.gateway.quinbook.service.impl;

import com.gateway.quinbook.dto.LoginRequestDTO;
import com.gateway.quinbook.dto.LoginResponseDTO;
import com.gateway.quinbook.entity.Login;
import com.gateway.quinbook.entity.Sessions;
import com.gateway.quinbook.repository.LoginRepository;
import com.gateway.quinbook.repository.SessionRepository;
import com.gateway.quinbook.service.LoginService;
import com.gateway.quinbook.util.CustomHash;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Service
public class LoginServiceIMPL implements LoginService {

    public static Properties getPropertiesOfKafka(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private SessionRepository sessionRepository;




    @Override
    public void insertNewLogin(String userName,String password) {
        Login login = new Login();
        login.setUserName(userName);
        login.setPassword(password);
        loginRepository.save(login);
    }


    @Override
    // TODO: pass UserValidation
    public LoginResponseDTO doLogin(LoginRequestDTO requestDTO) {

        if (requestDTO.getIsGoogle().equals("false")) {
            LoginResponseDTO responseDTO = new LoginResponseDTO();
            Optional<Login> optional = loginRepository.findById(requestDTO.getUserName());
            if (optional.isPresent()) {
                String hashedpassword = CustomHash.hashString(requestDTO.getPassword());
                hashedpassword = CustomHash.hashString(hashedpassword);
                boolean ans = (optional.get().getPassword().equals(hashedpassword));
                if (ans) {
                    Sessions sessions = new Sessions();

                    String sessionID = requestDTO.getUserName() + java.time.LocalDate.now().toString() + java.time.LocalTime.now().toString();
                    System.out.println("just before hashing " + sessionID);
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 6);
                    for (int i = 0; i < randomNum; i++) {
                        sessionID = CustomHash.hashString(sessionID);
                    }

                    sessions.setSessionID(sessionID);
                    sessions.setIsLoggedIn("true");
                    sessions.setUserName(requestDTO.getUserName());
                    sessionRepository.save(sessions);
                    responseDTO.setMessage("Success");
                    responseDTO.setSessionID(sessionID);
                    kafkaMethod(requestDTO.getUserName(), sessionID+" "+"true");
                    return responseDTO;
                } else {
                    responseDTO.setMessage("FAILEDDD");
                    responseDTO.setSessionID("");
                }
            }
            responseDTO.setMessage("FAILED");
            responseDTO.setSessionID("");
            return responseDTO;
        }
        //if isGoogle ; False
        else {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList("78840765485-unvn7jfjm8s58kn8fr6rpni9ffgqe6ah.apps.googleusercontent.com"))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();

            // (Receive idTokenString by HTTPS POST)
            try {
                LoginResponseDTO responseDTO = new LoginResponseDTO();
                GoogleIdToken idToken = verifier.verify(requestDTO.getToken());
                if (idToken != null) {
                    Payload payload = idToken.getPayload();

                    // Print user identifier
                    String userId = payload.getSubject();
                    System.out.println("User ID: " + userId);
                    Sessions sessions = new Sessions();
                    // Get profile information from payload
                    String email = payload.getEmail();
                    boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                    String name = (String) payload.get("name");
                    String pictureUrl = (String) payload.get("picture");
                    String locale = (String) payload.get("locale");
                    String familyName = (String) payload.get("family_name");
                    String givenName = (String) payload.get("given_name");


                    String emailId = email;
                    String[] splited = emailId.split("@");
                    emailId = splited[0];
                    sessions.setUserName(emailId);
                    String sessID = splited[0] + java.time.LocalDate.now().toString() + java.time.LocalTime.now().toString();
                    int random = ThreadLocalRandom.current().nextInt(1, 6);
                    for (int i = 0; i < random; i++) {
                        sessID = CustomHash.hashString(sessID);
                    }
                    sessions.setSessionID(sessID);
                    sessions.setIsLoggedIn("true");
                    sessionRepository.save(sessions);
                    kafkaMethod(emailId, sessID+" "+"true");

                    responseDTO.setIsRegistered(emailVerified);
                    responseDTO.setSessionID(sessID);

                    // Use or store profile information
                    // ...
                    return responseDTO;
                } else {
                    System.out.println("Invalid ID token.");
                    responseDTO.setMessage("invalid token");
                }
                return responseDTO;
            } catch (Exception e) {
                return null;

            }

        }

    }

    private void kafkaMethod(String userName ,String sessionID){
        Producer<String,String> producer = new KafkaProducer<>(getPropertiesOfKafka());
        producer.send(new ProducerRecord<>("session-details",userName,sessionID));
        producer.close();
    }
}
