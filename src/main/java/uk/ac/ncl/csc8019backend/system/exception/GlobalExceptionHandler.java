package uk.ac.ncl.csc8019backend.system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.ncl.csc8019backend.system.common.Result;
import uk.ac.ncl.csc8019backend.system.common.ResultCode;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Validation errors from @Valid on request body → unified Result response. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + (err.getDefaultMessage() != null ? err.getDefaultMessage() : "invalid"))
                .collect(Collectors.joining("; "));
        log.warn("Validation failed (request body): {}", message);
        return Result.validationFailed(message);
    }

    /** Validation errors from @Validated on request params / path variables → unified Result response. */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed (params): {}", message);
        return Result.validationFailed(message);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Result<?> handleResponseStatusException(ResponseStatusException e) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        if (status == HttpStatus.UNAUTHORIZED) {
            log.error("Unauthorized: {}", e.getReason());
            return Result.unauthorized();
        }
        if (status == HttpStatus.FORBIDDEN) {
            log.error("Forbidden: {}", e.getReason());
            return Result.forbidden();
        }
        log.error("Response status exception: {}", e.getMessage());
        return Result.failed(status.value(), e.getReason() != null ? e.getReason() : status.getReasonPhrase());
    }

    @ExceptionHandler(CustomException.class)
    public Result<?> handleCustomException(CustomException e) {
        log.error("Business Exception: {}", e.getMessage());
        return Result.failed(e.getResultCode().getCode(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access Denied: {}", e.getMessage());
        return Result.forbidden();
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication Failed: {}", e.getMessage());
        return Result.unauthorized();
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("Global Exception: ", e);
        return Result.failed(ResultCode.ERROR);
    }
}
