package moodbuddy.moodbuddy.domain.bookMark.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class BookMark extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "book_mark_id")
    private Long id;
}
