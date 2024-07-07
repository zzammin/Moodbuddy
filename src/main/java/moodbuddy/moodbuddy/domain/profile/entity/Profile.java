package moodbuddy.moodbuddy.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
@SuperBuilder
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "profile_nick_name", columnDefinition = "varchar(255)")
    private String profileNickName;

    @Column(name = "profile_comment", columnDefinition = "varchar(255)")
    private String profileComment = "";

    @Column(name = "kakao_id", columnDefinition = "bigint")
    private Long kakaoId;

    protected Profile() {}
}