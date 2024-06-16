package moodbuddy.moodbuddy.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String userEmail;
    private String userName;

    private String userNickName;

    private String userRole = "ROLE_USER";
}
