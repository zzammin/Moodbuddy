package moodbuddy.moodbuddy.global.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {


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


