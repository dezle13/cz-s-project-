package com.example.bms.battery.common.exception;



import com.example.bms.battery.inter.model.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice //统一处理
public class GlobalExceptionHandlerAdvice {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    /**
     * 处理 @Validated 注解引发的参数校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage<?> handleValidationExceptions(Exception ex) {
        StringBuilder errorMessage = new StringBuilder();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validEx = (MethodArgumentNotValidException) ex;
            for (FieldError error : validEx.getBindingResult().getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException violationEx = (ConstraintViolationException) ex;
            for (ConstraintViolation<?> violation : violationEx.getConstraintViolations()) {
                errorMessage.append(violation.getMessage()).append("; ");
            }
        }

        return ResponseMessage.error(errorMessage.toString().trim());
    }

//    /**
//     * 处理所有未捕获的异常
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseMessage handleAllExceptions(Exception ex) {
//        log.error("未知异常: {}", ex.getMessage(), ex);
//        return ResponseMessage.error( ex.getMessage());
//    }

//    @ExceptionHandler({Exception.class})///什么异常的统一处理
//    public ResponseMessage handlerException(Exception e, HttpServletRequest request, HttpServletResponse response){///同一格式
//        log.error("统一异常",e);
//        return new ResponseMessage(500,"error", null);
//
//    }
}
