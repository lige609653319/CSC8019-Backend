package uk.ac.ncl.csc8019backend.system.exception;

import lombok.Getter;
import uk.ac.ncl.csc8019backend.system.common.ResultCode;

@Getter
public class CustomException extends RuntimeException {
    private final ResultCode resultCode;

    public CustomException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILED;
    }

    public CustomException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public CustomException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
}
