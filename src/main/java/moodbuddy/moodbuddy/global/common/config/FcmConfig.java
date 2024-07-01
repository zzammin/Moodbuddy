package moodbuddy.moodbuddy.global.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FcmConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase.json")) {
            if (serviceAccount == null) {
                throw new FileNotFoundException("firebase.json 파일을 찾을 수 없습니다.");
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }
}