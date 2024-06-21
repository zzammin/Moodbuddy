package moodbuddy.moodbuddy.domain.temporaryDiaryImage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class TemporaryDiaryImage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "temporary_diary_image_id")
    private Long id;
}
