package project.ricecake.error.exception.notfound;

import project.ricecake.error.ErrorCode;
import project.ricecake.error.exception.BusinessException;

public class NotFoundException extends BusinessException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
