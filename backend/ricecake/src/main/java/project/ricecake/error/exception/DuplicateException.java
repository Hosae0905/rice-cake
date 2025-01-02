package project.ricecake.error.exception;

import project.ricecake.error.ErrorCode;

public class DuplicateException extends BusinessException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public DuplicateException() {
        super(ErrorCode.DUPLICATE);
    }
}
