package moodbuddy.moodbuddy.domain.user.scheduler;

import lombok.RequiredArgsConstructor;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Component
@RequiredArgsConstructor
public class UserScheduler {
    private final UserRepository userRepository;
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCheckTodayDiary() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setCheckTodayDairy(true);
        }
    }
}
