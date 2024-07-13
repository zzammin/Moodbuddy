package moodbuddy.moodbuddy.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmMessageDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmResDTO;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class FcmServiceImpl implements FcmService {

    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public FcmResDTO sendMessageTo(FcmReqDTO fcmReqDTO) {
        log.info("[FcmService] sendMessage");
        try {
            String message = makeMessage(fcmReqDTO);
            String API_URL = "https://fcm.googleapis.com/v1/projects/moodbuddy-d8bfa/messages:send";

            RequestBody body = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + getAccessToken())
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Unexpected code " + response);
                }

                log.info("FCM response status: " + response.code());

                return FcmResDTO.builder()
                        .title(fcmReqDTO.getTitle())
                        .body(fcmReqDTO.getBody())
                        .build();
            }
        } catch (Exception e) {
            log.error("[FcmService] sendMessage error", e);
            log.error("[FcmService] 오류 발생 토큰 : " + fcmReqDTO.getToken());
            throw new RuntimeException(e);
        }
    }

    private String makeMessage(FcmReqDTO fcmReqDTO) {
        log.info("[FcmService] makeMessage");
        try {
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
        } catch (Exception e) {
            log.error("[FcmService] makeMessage error", e);
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken() {
        log.info("[FcmService] getAccessToken");
        try {
            String firebaseConfigPath = "firebase.json";
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
}
