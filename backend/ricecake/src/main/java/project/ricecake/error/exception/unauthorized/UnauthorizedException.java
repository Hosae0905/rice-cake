package project.ricecake.error.exception.unauthorized;

import project.ricecake.error.ErrorCode;
import project.ricecake.error.exception.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
