package moodbuddy.moodbuddy.global.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

// 애플리케이션 시작 시 firebase 초기화
@Configuration
@Slf4j
public class FcmConfig {

    @PostConstruct
    public void initializeFirebase(){
        try {
            InputStream serviceAccount = new ClassPathResource("firebase/firebase.json").getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) { // FirebaseApp이 이미 초기화되어 있지 않은 경우에만 초기화 실행
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e){
            log.error("fcm error : ",e);;
        }
    }
}