package moodbuddy.moodbuddy.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KakaoTokenDto {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("refresh_token")
    private String refresh_token;
    @JsonProperty("expires_in")
    private int expires_in;
    @JsonProperty("refresh_token_expires_in")
    private int refresh_token_expires_in;
    @JsonProperty("scope")
    private String scope;
}

