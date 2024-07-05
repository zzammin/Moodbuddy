package moodbuddy.moodbuddy.domain.profileImage.entity;

import jakarta.persistence.*;
import lombok.*;
import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id")
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;

    @Column(name = "profile_img_url", columnDefinition = "varchar(255)")
    private String profileImgURL = "";
}