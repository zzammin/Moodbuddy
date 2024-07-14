package moodbuddy.moodbuddy.global.common.config;

import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.user.dto.fcm.FcmReqDTO;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.domain.user.service.FcmService;
import moodbuddy.moodbuddy.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class UserAlarmConfig implements SchedulingConfigurer {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("UserAlarmScheduler-");
        scheduler.initialize();
        return scheduler;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private FcmService fcmService;

    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
        List<User> users = userService.getAllUsersWithAlarms();
        for (User user : users) {
            if (Boolean.TRUE.equals(user.getAlarm())) {
                String alarmTime = user.getAlarmTime();
                try {
                    LocalTime time;
                    if (alarmTime == null || alarmTime.isEmpty()) {
                        log.warn("User {} has empty alarm time. Setting default time to 12:00.", user.getUserId());
                        time = LocalTime.NOON;
                    } else {
                        time = LocalTime.parse(alarmTime);
                    }

                    taskRegistrar.addTriggerTask(
                            new UserAlarmRunnableTask(user, fcmService),
                            triggerContext -> {
                                ZonedDateTime nextExecutionTime = ZonedDateTime.now().plusDays(1)
                                        .withHour(time.getHour())
                                        .withMinute(time.getMinute())
                                        .withSecond(0)
                                        .withNano(0);
                                return nextExecutionTime.toInstant();
                            }
                    );
                } catch (DateTimeParseException e) {
                    log.error("Invalid alarm time for user {}: {}", user.getUserId(), alarmTime, e);
                }
            }
        }
    }

    // UserAlarmRunnableTask 클래스 정의
    private static class UserAlarmRunnableTask implements Runnable {
        private final User user;
        private final FcmService fcmService;

        public UserAlarmRunnableTask(User user, FcmService fcmService) {
            this.user = user;
            this.fcmService = fcmService;
        }

        @Override
        public void run() {
            try {
                fcmService.sendMessageTo(FcmReqDTO.builder()
                        .token(user.getFcmToken())
                        .title("moodbuddy : 일기를 작성할 시간이에요!")
                        .body("")
                        .build());
            } catch (Exception e) {
                log.error("[UserAlarmConfig] run error", e);
                throw new RuntimeException(e);
            }
        }
    }
}
