package project.ricecake.error.exception;

import project.ricecake.error.ErrorCode;

public class InvalidPasswordException extends UnauthorizedException {
    public InvalidPasswordException() {
        super(ErrorCode.MISMATCH_PASSWORD);
    }
}
