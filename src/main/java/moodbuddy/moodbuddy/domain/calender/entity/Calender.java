package moodbuddy.moodbuddy.domain.calender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class Calender extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "calender_id")
    private Long id;
}
