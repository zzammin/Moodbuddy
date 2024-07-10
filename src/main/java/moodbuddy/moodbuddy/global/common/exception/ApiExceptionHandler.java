package moodbuddy.moodbuddy.global.common.exception;

import moodbuddy.moodbuddy.global.common.exception.database.DatabaseNullOrEmptyException;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryNoAccessException;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryNotFoundException;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryTodayExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(DatabaseNullOrEmptyException.class)
    public ResponseEntity<ApiErrorResponse> handleException(DatabaseNullOrEmptyException ex){
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        "JED-001",
                        ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    @ExceptionHandler(DiaryTodayExistingException.class)
    public ResponseEntity<ApiErrorResponse> handleException(DiaryTodayExistingException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        errorCode.getErrorCode(),
                        errorCode.getMessage()),
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }
    @ExceptionHandler(DiaryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(DiaryNotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        errorCode.getErrorCode(),
                        errorCode.getMessage()),
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }
    @ExceptionHandler(DiaryNoAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleException(DiaryNoAccessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                new ApiErrorResponse(
                        errorCode.getErrorCode(),
                        errorCode.getMessage()),
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }
}


//    사용 예시
//    1. exception class 정의
//    2. api exception handler에 handleException 함수 선언(생성자 오버로딩)
//    3. service layer에서 custom exception 처리
//    4. 실패 case에서 custom exception이 return 되는지 확인해보기
//        @ExceptionHandler(ParameterNullOrEmptyException.class)
//        public ResponseEntity<ApiErrorResponse> handleException(ParameterNullOrEmptyException ex) {
//                return new ResponseEntity<>(
//                new ApiErrorResponse(
//                "JEP-001",
//                ex.getMessage()),
//                HttpStatus.BAD_REQUEST
//                );
//                }
//
//        @ExceptionHandler(DatabaseNullOrEmptyException.class)
//        public ResponseEntity<ApiErrorResponse> handleException(DatabaseNullOrEmptyException ex){
//                return new ResponseEntity<>(
//                new ApiErrorResponse(
//                "JED-001",
//                ex.getMessage()),
//                HttpStatus.INTERNAL_SERVER_ERROR
//                );
//                }


