package moodbuddy.moodbuddy.domain.user.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmMessageDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmResDTO;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${firebase.service-account-file}")
    private String firebaseServiceAccountFile;

    @Override
    public FcmResDTO sendMessageTo(FcmReqDTO fcmReqDTO) {
        log.info("[FcmService] sendMessageTo");
//        Message message = Message.builder()
//                .putData("title", fcmReqDTO.getTitle())
//                .putData("body", fcmReqDTO.getBody())
//                .setToken(fcmReqDTO.getToken())
//                .build();
//
//        log.info("Sending FCM message to token: " + fcmReqDTO.getToken());
//        log.info("Message title: " + fcmReqDTO.getTitle());
//        log.info("Message body: " + fcmReqDTO.getBody());
//
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

            String API_URL = "https://fcm.googleapis.com/v1/projects/moodbuddy-e57b9/messages:send";
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            // 응답 상태 코드 및 본문 로그 출력
            log.info("response Status Code: " + response.getStatusCode());
            log.info("response Body: " + response.getBody());

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
            String firebaseConfigPath = firebaseServiceAccountFile;

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