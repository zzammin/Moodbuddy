package moodbuddy.moodbuddy.domain.letter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class Letter extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "letter_id")
    private Long id;
}
