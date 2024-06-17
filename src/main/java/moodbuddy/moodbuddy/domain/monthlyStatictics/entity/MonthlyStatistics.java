package moodbuddy.moodbuddy.domain.monthlyStatictics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class MonthlyStatistics extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "monthly_statistics_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 월별 통계 잠시 스탑
}
