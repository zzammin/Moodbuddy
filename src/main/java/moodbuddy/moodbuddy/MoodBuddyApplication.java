package moodbuddy.moodbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoodBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodBuddyApplication.class, args);
    }

}