package moodbuddy.moodbuddy.domain.quddyTI.entity;

import jakarta.persistence.*;
import lombok.*;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "quddy_ti")
public class QuddyTI extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kakao_id", nullable = false, columnDefinition = "bigint")
    private Long kakaoId;

    /** 감정 갯수 **/
    @Column(name = "happiness_count", nullable = false, columnDefinition = "int")
    private int happinessCount;
    @Column(name = "anger_count", nullable = false, columnDefinition = "int")
    private int angerCount;
    @Column(name = "disgust_count", nullable = false, columnDefinition = "int")
    private int disgustCount;
    @Column(name = "fear_count", nullable = false, columnDefinition = "int")
    private int fearCount;
    @Column(name = "neutral_count", nullable = false, columnDefinition = "int")
    private int neutralCount;
    @Column(name = "sadness_count", nullable = false, columnDefinition = "int")
    private int sadnessCount;
    @Column(name = "surprise_count", nullable = false, columnDefinition = "int")
    private int surpriseCount;

    /** 주제 갯수 **/
    @Column(name = "daily_count", nullable = false, columnDefinition = "int")
    private int dailyCount;
    @Column(name = "growth_count", nullable = false, columnDefinition = "int")
    private int growthCount;
    @Column(name = "emotion_count", nullable = false, columnDefinition = "int")
    private int emotionCount;
    @Column(name = "travel_count", nullable = false, columnDefinition = "int")
    private int travelCount;

    @Column(name = "quddy_ti_type", nullable = false, columnDefinition = "varchar(10)")
    private String quddyTIType ;
}
