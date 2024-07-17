package moodbuddy.moodbuddy.domain.diary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDiary is a Querydsl query type for Diary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiary extends EntityPathBase<Diary> {

    private static final long serialVersionUID = 35460175L;

    public static final QDiary diary = new QDiary("diary");

    public final moodbuddy.moodbuddy.global.common.base.QBaseEntity _super = new moodbuddy.moodbuddy.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final BooleanPath diaryBookMarkCheck = createBoolean("diaryBookMarkCheck");

    public final StringPath diaryContent = createString("diaryContent");

    public final DatePath<java.time.LocalDate> diaryDate = createDate("diaryDate", java.time.LocalDate.class);

    public final EnumPath<DiaryEmotion> diaryEmotion = createEnum("diaryEmotion", DiaryEmotion.class);

    public final EnumPath<DiaryStatus> diaryStatus = createEnum("diaryStatus", DiaryStatus.class);

    public final EnumPath<DiarySubject> diarySubject = createEnum("diarySubject", DiarySubject.class);

    public final StringPath diarySummary = createString("diarySummary");

    public final StringPath diaryTitle = createString("diaryTitle");

    public final EnumPath<DiaryWeather> diaryWeather = createEnum("diaryWeather", DiaryWeather.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QDiary(String variable) {
        super(Diary.class, forVariable(variable));
    }

    public QDiary(Path<? extends Diary> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDiary(PathMetadata metadata) {
        super(Diary.class, metadata);
    }

}

