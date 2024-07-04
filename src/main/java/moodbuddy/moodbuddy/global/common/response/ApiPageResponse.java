package moodbuddy.moodbuddy.global.common.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPageResponse<T> {
    @Schema(description = "페이지 번호", example = "0")
    private int pageNumber;

    @Schema(description = "페이지 크기", example = "10")
    private int pageSize;

    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPages;

    @Schema(description = "전체 요소 수", example = "50")
    private long totalElements;

    @Schema(description = "현재 페이지의 요소 수", example = "10")
    private int numberOfElements;

    @ArraySchema(schema = @Schema(implementation = DiaryResDetailDTO.class))
    @Schema(description = "페이지 데이터", implementation = DiaryResDetailDTO.class)
    private List<T> data;

    public static <T> ApiPageResponse<T> from(Page<T> page) {
        return new ApiPageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumberOfElements(),
                page.getContent()
        );
    }
}
