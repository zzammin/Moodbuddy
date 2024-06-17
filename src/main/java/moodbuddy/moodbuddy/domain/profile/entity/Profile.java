package moodbuddy.moodbuddy.domain.profile.entity;

import jakarta.persistence.*;
import lombok.Getter;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "profile_id")
    private Long id;
    private String profileNickName;
    private String profileComment = "";
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    /** 생성자 **/
    protected Profile() {}
}
