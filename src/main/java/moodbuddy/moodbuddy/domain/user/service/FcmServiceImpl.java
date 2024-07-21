package moodbuddy.moodbuddy.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmMessageDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmResDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class FcmServiceImpl implements FcmService {
    @Override
    public FcmResDTO sendMessageTo(FcmReqDTO fcmReqDTO) {
//        Message message = Message.builder()
//                .putData("title", fcmReqDTO.getTitle())
//                .putData("body", fcmReqDTO.getBody())
//                .setToken(fcmReqDTO.getToken())
//                .build();
//        try{
//            String response = FirebaseMessaging.getInstance().send(message);
//            return FcmResDTO.builder()
//                    .response(response)
//                    .title(fcmReqDTO.getTitle())
//                    .body(fcmReqDTO.getBody())
//                    .build();
//        } catch (FirebaseMessagingException e){
//            throw new RuntimeException(e);
//        }
        try {
            log.info("fcmReqDTO.getToken() : "+fcmReqDTO.getToken());
            log.info("fcmReqDTO.getTitle() : "+fcmReqDTO.getTitle());
            log.info("fcmReqDTO.getBody() : "+fcmReqDTO.getBody());
            String message = makeMessage(fcmReqDTO);
            log.info("message : "+message);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());
            log.info("headers : "+headers);
            log.info("getAccessToken() : "+getAccessToken());
            HttpEntity<String> entity = new HttpEntity<>(message, headers);

            String API_URL = "https://fcm.googleapis.com/v1/projects/moodbuddy3/messages:send";
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            System.out.println(response.getStatusCode());

            return FcmResDTO.builder()
                    .title(fcmReqDTO.getTitle())
                    .body(fcmReqDTO.getBody())
                    .build();
        } catch (Exception e) {
            log.error("[FcmService] sendMessageTo error", e);
            log.error("[FcmService] 오류 발생 토큰 : " + fcmReqDTO.getToken());
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken() {
        try {
            String firebaseConfigPath = "firebase/firebase.json";

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            log.error("[FcmService] getAccessToken error", e);
            throw new RuntimeException(e);
        }
    }

    private String makeMessage(FcmReqDTO fcmReqDTO) {
        try {
            ObjectMapper om = new ObjectMapper();
            FcmMessageDTO fcmMessageDto = FcmMessageDTO.builder()
                    .message(FcmMessageDTO.Message.builder()
                            .token(fcmReqDTO.getToken())
                            .notification(FcmMessageDTO.Notification.builder()
                                    .title(fcmReqDTO.getTitle())
                                    .body(fcmReqDTO.getBody())
                                    .image(null)
                                    .build()
                            ).build()).validateOnly(false).build();

            return om.writeValueAsString(fcmMessageDto);
        } catch (Exception e) {
            log.error("[FcmService] makeMessage error", e);
            throw new RuntimeException(e);
        }
    }
}

//    @Override
//    public FcmResDTO sendMessageTo(FcmReqDTO fcmReqDTO) {
//        log.info("[FcmService] sendMessage");
//        try {
//            log.info("fcmReqDTO.getToken() : "+fcmReqDTO.getToken());
//            log.info("fcmReqDTO.getTitle() : "+fcmReqDTO.getTitle());
//            log.info("fcmReqDTO.getBody() : "+fcmReqDTO.getBody());
//            String message = makeMessage(fcmReqDTO);
//            log.info("message : "+message);
//            String API_URL = "https://fcm.googleapis.com/v1/projects/moodbuddy-d8bfa/messages:send";
//
//            RequestBody body = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//            log.info("body : "+body);
//            log.info("getAccessToken() : "+getAccessToken());
//            Request request = new Request.Builder()
//                    .url(API_URL)
//                    .post(body)
//                    .addHeader("Authorization", "Bearer " + getAccessToken())
//                    .build();
//
//            log.info("request : "+request);
//            log.info("httpClient.newCall(request).execute() : "+httpClient.newCall(request).execute());
//            try (Response response = httpClient.newCall(request).execute()) {
//                if (!response.isSuccessful()) {
//                    throw new RuntimeException("Unexpected code " + response);
//                }
//
//                log.info("FCM response status: " + response.code());
//
//                return FcmResDTO.builder()
//                        .title(fcmReqDTO.getTitle())
//                        .body(fcmReqDTO.getBody())
//                        .build();
//            }
//        } catch (Exception e) {
//            log.error("[FcmService] sendMessage error", e);
//            log.error("[FcmService] 오류 발생 토큰 : " + fcmReqDTO.getToken());
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String makeMessage(FcmReqDTO fcmReqDTO) {
//        log.info("[FcmService] makeMessage");
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            FcmMessageDTO fcmMessageDTO = FcmMessageDTO.builder()
//                    .message(FcmMessageDTO.Message.builder()
//                            .token(fcmReqDTO.getToken())
//                            .notification(FcmMessageDTO.Notification.builder()
//                                    .title(fcmReqDTO.getTitle())
//                                    .body(fcmReqDTO.getBody())
//                                    .image(null)
//                                    .build()
//                            ).build()).validateOnly(false).build();
//
//            log.info("fcmMessageDTO : "+fcmMessageDTO);
//
//            return objectMapper.writeValueAsString(fcmMessageDTO);
//        } catch (Exception e) {
//            log.error("[FcmService] makeMessage error", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String getAccessToken() {
//        log.info("[FcmService] getAccessToken");
//        try {
//            String firebaseConfigPath = "firebase.json";
//            GoogleCredentials googleCredentials = GoogleCredentials
//                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
//                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//
//            googleCredentials.refreshIfExpired();
//            log.info("googleCredentials.getAccessToken().getTokenValue() : "+googleCredentials.getAccessToken().getTokenValue());
//            return googleCredentials.getAccessToken().getTokenValue();
//        } catch (Exception e) {
//            log.error("[FcmService] getAccessToken error", e);
//            throw new RuntimeException(e);
//        }
//    }
//}