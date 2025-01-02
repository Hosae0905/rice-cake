package project.ricecake.error.exception;

import project.ricecake.error.ErrorCode;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
