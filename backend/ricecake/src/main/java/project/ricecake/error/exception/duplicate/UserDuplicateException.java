package project.ricecake.error.exception.duplicate;

import project.ricecake.error.ErrorCode;
import project.ricecake.error.exception.duplicate.DuplicateException;

public class UserDuplicateException extends DuplicateException {
    public UserDuplicateException() {
        super(ErrorCode.DUPLICATE_USER);
    }
}
