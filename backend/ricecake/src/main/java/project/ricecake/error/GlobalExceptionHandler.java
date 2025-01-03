package project.ricecake.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.ricecake.error.exception.BusinessException;
import project.ricecake.error.exception.duplicate.DuplicateException;
import project.ricecake.error.exception.notfound.NotFoundException;
import project.ricecake.error.exception.unauthorized.UnauthorizedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        HttpStatusCode status = e.getStatusCode();
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ErrorCode.INVALID_INPUT_VALUE, fieldError.getDefaultMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorCode);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(DuplicateException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorCode);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorCode);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorCode);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, status);
    }
}
