package moodbuddy.moodbuddy.domain.letter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "letter")
public class Letter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "letter_format", nullable = false)
    private Integer letterFormat;

    @Lob
    @Column(name = "worry_content")
    private String letterWorryContent;

    @Lob
    @Column(name = "answer_content")
    private String letterAnswerContent;

    @Column(name = "letter_date", nullable = false)
    private LocalDateTime letterDate;
}
