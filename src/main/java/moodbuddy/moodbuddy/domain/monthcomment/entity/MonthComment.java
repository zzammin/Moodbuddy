package moodbuddy.moodbuddy.domain.monthcomment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "month_comment")
public class MonthComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kakao_id", nullable = false, columnDefinition = "bigint")
    private Long kakaoId;

    @Column(name = "comment_date", columnDefinition = "varchar(255)")
    private String commentDate;

    @Column(name = "comment_content", columnDefinition = "text")
    private String commentContent;
}
