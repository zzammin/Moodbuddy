package moodbuddy.moodbuddy.global;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "HealthCheck", description = "헬스 체 API")
@RequiredArgsConstructor
@Slf4j
public class HealthCheckApiController
{
    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck() {
        log.info("[HealthCheckApiController] healthCheck");
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(HttpStatus.CREATED.value(), "[SUCCESS] 202407111328 healthCheck"));
    }
}
