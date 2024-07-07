package moodbuddy.moodbuddy.domain.profileImage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfileImage is a Querydsl query type for ProfileImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfileImage extends EntityPathBase<ProfileImage> {

    private static final long serialVersionUID = -341054483L;

    public static final QProfileImage profileImage = new QProfileImage("profileImage");

    public final moodbuddy.moodbuddy.global.common.base.QBaseEntity _super = new moodbuddy.moodbuddy.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath profileImgURL = createString("profileImgURL");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QProfileImage(String variable) {
        super(ProfileImage.class, forVariable(variable));
    }

    public QProfileImage(Path<? extends ProfileImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfileImage(PathMetadata metadata) {
        super(ProfileImage.class, metadata);
    }

}

