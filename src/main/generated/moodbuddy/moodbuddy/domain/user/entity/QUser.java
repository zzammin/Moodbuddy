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

    public final moodbuddy.moodbuddy.global.common.base.QBaseEntity _super = new moodbuddy.moodbuddy.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public final StringPath userBirth = createString("userBirth");

    public final NumberPath<Integer> userCurDiaryNums = createNumber("userCurDiaryNums", Integer.class);

    public final StringPath userEmail = createString("userEmail");

    public final NumberPath<Integer> userLastDiaryNums = createNumber("userLastDiaryNums", Integer.class);

    public final NumberPath<Integer> userLetterNums = createNumber("userLetterNums", Integer.class);

    public final StringPath userName = createString("userName");

    public final StringPath userNickName = createString("userNickName");

    public final DateTimePath<java.time.LocalDateTime> userNoticeTime = createDateTime("userNoticeTime", java.time.LocalDateTime.class);

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

