package moodbuddy.moodbuddy.domain.quddyTI.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuddyTI is a Querydsl query type for QuddyTI
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuddyTI extends EntityPathBase<QuddyTI> {

    private static final long serialVersionUID = -450855057L;

    public static final QQuddyTI quddyTI = new QQuddyTI("quddyTI");

    public final moodbuddy.moodbuddy.global.common.base.QBaseEntity _super = new moodbuddy.moodbuddy.global.common.base.QBaseEntity(this);

    public final NumberPath<Integer> angerCount = createNumber("angerCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Integer> dailyCount = createNumber("dailyCount", Integer.class);

    public final NumberPath<Integer> disgustCount = createNumber("disgustCount", Integer.class);

    public final NumberPath<Integer> emotionCount = createNumber("emotionCount", Integer.class);

    public final NumberPath<Integer> fearCount = createNumber("fearCount", Integer.class);

    public final NumberPath<Integer> growthCount = createNumber("growthCount", Integer.class);

    public final NumberPath<Integer> happinessCount = createNumber("happinessCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    public final NumberPath<Integer> neutralCount = createNumber("neutralCount", Integer.class);

    public final StringPath quddyTIType = createString("quddyTIType");

    public final NumberPath<Integer> sadnessCount = createNumber("sadnessCount", Integer.class);

    public final NumberPath<Integer> surpriseCount = createNumber("surpriseCount", Integer.class);

    public final NumberPath<Integer> travelCount = createNumber("travelCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedTime = _super.updatedTime;

    public QQuddyTI(String variable) {
        super(QuddyTI.class, forVariable(variable));
    }

    public QQuddyTI(Path<? extends QuddyTI> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuddyTI(PathMetadata metadata) {
        super(QuddyTI.class, metadata);
    }

}

