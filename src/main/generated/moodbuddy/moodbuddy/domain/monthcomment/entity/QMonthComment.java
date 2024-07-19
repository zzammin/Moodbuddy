package moodbuddy.moodbuddy.domain.monthcomment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMonthComment is a Querydsl query type for MonthComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMonthComment extends EntityPathBase<MonthComment> {

    private static final long serialVersionUID = 571588775L;

    public static final QMonthComment monthComment = new QMonthComment("monthComment");

    public final moodbuddy.moodbuddy.global.common.base.QBaseEntity _super = new moodbuddy.moodbuddy.global.common.base.QBaseEntity(this);

    public final StringPath commentContent = createString("commentContent");

    public final StringPath commentDate = createString("commentDate");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QMonthComment(String variable) {
        super(MonthComment.class, forVariable(variable));
    }

    public QMonthComment(Path<? extends MonthComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMonthComment(PathMetadata metadata) {
        super(MonthComment.class, metadata);
    }

}

