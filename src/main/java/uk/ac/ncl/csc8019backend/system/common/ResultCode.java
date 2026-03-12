package uk.ac.ncl.csc8019backend.system.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "Success"),
    ERROR(500, "Internal Server Error"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    VALIDATE_FAILED(404, "Not Found"),
    BAD_REQUEST(400, "Bad Request"),
    FAILED(500, "Failed");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
