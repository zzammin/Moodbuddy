package moodbuddy.moodbuddy.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private ResponseStatus status;
    private T data;

    public ApiResponse(Integer code, ResponseStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Integer code, ResponseStatus status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public static <T> ApiResponse<T> SUCCESS (Integer code, String message) {
        return new ApiResponse(code, ResponseStatus.SUCCESS, message);
    }
    public static <T> ApiResponse<T> SUCCESS (Integer code, String message, T data) {
        return new ApiResponse(code, ResponseStatus.SUCCESS, message, data);
    }

    public static <T> ApiResponse<T> FAILURE (Integer code, String message) {
        return new ApiResponse(code, ResponseStatus.FAIL, message);
    }

    public static <T> ApiResponse<T> ERROR (Integer code, String message) {
        return new ApiResponse(code, ResponseStatus.ERROR, message);
    }
}
