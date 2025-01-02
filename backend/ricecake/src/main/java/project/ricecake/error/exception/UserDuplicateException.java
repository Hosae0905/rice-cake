package project.ricecake.error.exception;

import project.ricecake.error.ErrorCode;

public class UserDuplicateException extends DuplicateException {
    public UserDuplicateException() {
        super(ErrorCode.DUPLICATE_USER);
    }
}
