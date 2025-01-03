package project.ricecake.error.exception.duplicate;

import project.ricecake.error.ErrorCode;
import project.ricecake.error.exception.BusinessException;

public class DuplicateException extends BusinessException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public DuplicateException() {
        super(ErrorCode.DUPLICATE);
    }
}
