package moodbuddy.moodbuddy.domain.profile.entity;

import jakarta.persistence.*;
import lombok.Getter;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "profile_nick_name", columnDefinition = "varchar(255)")
    private String profileNickName;

    @Column(name = "profile_comment", columnDefinition = "varchar(255)")
    private String profileComment = "";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Profile() {}
}