package moodbuddy.moodbuddy.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmMessageDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmResDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class FcmServiceImpl implements FcmService{
    @Override
    public FcmResDTO sendMessage(FcmReqDTO fcmReqDTO){
        log.info("[FcmService] sendMessage");
        try{
            String message = makeMessage(fcmReqDTO);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+getAccessToken());

            HttpEntity<String> entity = new HttpEntity<>(message,headers);
            String API_URL = "https://fcm.googleapis.com/v1/projects/moodbuddy-d8bfa/messages:send";
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            System.out.println(response.getStatusCode());

            return FcmResDTO.builder()
                    .title(fcmReqDTO.getTitle())
                    .body(fcmReqDTO.getBody())
                    .build();
        } catch (Exception e){
            log.error("[FcmService] sendMessage error",e);
            log.error("[FcmService] 오류 발생 토큰 : " + fcmReqDTO.getToken());
            throw new RuntimeException(e);
        }
    }

    private String makeMessage(FcmReqDTO fcmReqDTO){
        log.info("[FcmService] makeMessage");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            FcmMessageDTO fcmMessageDTO = FcmMessageDTO.builder()
                    .message(FcmMessageDTO.Message.builder()
                            .token(fcmReqDTO.getToken())
                            .notification(FcmMessageDTO.Notification.builder()
                                    .title(fcmReqDTO.getTitle())
                                    .body(fcmReqDTO.getBody())
                                    .image(null)
                                    .build()
                            ).build()).validateOnly(false).build();

            return objectMapper.writeValueAsString(fcmMessageDTO);
        } catch (Exception e){
            log.error("[FcmService] makeMessage error",e);
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken(){
        log.info("[FcmService] getAccessToken");
        try{
            String firebaseConfigPath = "firebase.json";
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            log.error("[FcmService] getAccessToken error",e);
            throw new RuntimeException(e);
        }
    }
}
