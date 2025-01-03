package project.ricecake.error.exception.notfound;

import project.ricecake.error.ErrorCode;
import project.ricecake.error.exception.notfound.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
