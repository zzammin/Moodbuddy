package moodbuddy.moodbuddy.domain.profileImage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;
@Entity
@Getter
public class ProfileImage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "profile_image_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    private String profileImgURL = "";
    /** 생성자 **/
    protected ProfileImage() {}
}
