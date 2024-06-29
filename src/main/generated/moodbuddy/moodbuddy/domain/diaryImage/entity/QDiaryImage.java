package moodbuddy.moodbuddy.domain.diaryImage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiaryImage is a Querydsl query type for DiaryImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiaryImage extends EntityPathBase<DiaryImage> {

    private static final long serialVersionUID = -397457287L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDiaryImage diaryImage = new QDiaryImage("diaryImage");

    public final moodbuddy.moodbuddy.global.common.base.QBaseEntity _super = new moodbuddy.moodbuddy.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final moodbuddy.moodbuddy.domain.diary.entity.QDiary diary;

    public final StringPath diaryImgURL = createString("diaryImgURL");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QDiaryImage(String variable) {
        this(DiaryImage.class, forVariable(variable), INITS);
    }

    public QDiaryImage(Path<? extends DiaryImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDiaryImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDiaryImage(PathMetadata metadata, PathInits inits) {
        this(DiaryImage.class, metadata, inits);
    }

    public QDiaryImage(Class<? extends DiaryImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.diary = inits.isInitialized("diary") ? new moodbuddy.moodbuddy.domain.diary.entity.QDiary(forProperty("diary")) : null;
    }

}

