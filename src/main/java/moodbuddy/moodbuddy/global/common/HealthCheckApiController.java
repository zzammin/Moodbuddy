package moodbuddy.moodbuddy.global.common;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/healthCheck")
@RequiredArgsConstructor
@Slf4j
public class HealthCheckApiController {
    @GetMapping
    public ResponseEntity<?> healthcheck() {
        return ResponseEntity.ok().body("202406291837 healthCheck 완료.");
    }
}

