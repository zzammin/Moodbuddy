package moodbuddy.moodbuddy.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReqLoginDTO {
    @Schema(description = "임시로 사용할 자체 로그인이기 때문에 kakaoId 으로만 로그인이 가능하게 한다.", example = "12342")
    private Long kakaoId;
}
