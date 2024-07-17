package moodbuddy.moodbuddy.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -655893857L;

    public static final QUser user = new QUser("user");

    public final StringPath accessToken = createString("accessToken");

    public final DatePath<java.time.LocalDate> accessTokenExpiredAt = createDate("accessTokenExpiredAt", java.time.LocalDate.class);

    public final BooleanPath alarm = createBoolean("alarm");

    public final StringPath alarmTime = createString("alarmTime");

    public final StringPath birthday = createString("birthday");

    public final BooleanPath checkTodayDairy = createBoolean("checkTodayDairy");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath fcmToken = createString("fcmToken");

    public final BooleanPath gender = createBoolean("gender");

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath refreshToken = createString("refreshToken");

    public final DatePath<java.time.LocalDate> refreshTokenExpiredAt = createDate("refreshTokenExpiredAt", java.time.LocalDate.class);

    public final NumberPath<Integer> userCurDiaryNums = createNumber("userCurDiaryNums", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final NumberPath<Integer> userLastDiaryNums = createNumber("userLastDiaryNums", Integer.class);

    public final NumberPath<Integer> userLetterNums = createNumber("userLetterNums", Integer.class);

    public final StringPath userRole = createString("userRole");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

