package moodbuddy.moodbuddy.domain.profile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
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
    /** 생성자 **/
    protected Profile() {}
}
