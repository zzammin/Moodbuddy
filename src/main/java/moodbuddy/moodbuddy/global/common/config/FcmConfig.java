package moodbuddy.moodbuddy.global.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
@Slf4j
public class FcmConfig {

    @Value("${firebase.service-account-file}")
    private String firebaseServiceAccountFile;

    @PostConstruct
    public void initializeFirebase() {
        try {
            log.info("Initializing Firebase with service account file: {}", firebaseServiceAccountFile);
            ClassPathResource resource = new ClassPathResource(firebaseServiceAccountFile);
            if (!resource.exists()) {
                log.error("Service account file does not exist: {}", firebaseServiceAccountFile);
                return;
            }
            InputStream serviceAccount = resource.getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            log.info("Fcm try");
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Fcm Setting Completed");
            } else {
                log.info("FirebaseApp already initialized");
            }
        } catch (Exception e) {
            log.error("fcm error: ", e);
        }
    }
}
