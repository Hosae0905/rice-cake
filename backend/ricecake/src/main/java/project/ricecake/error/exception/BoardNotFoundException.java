package project.ricecake.error.exception;

import project.ricecake.error.ErrorCode;

public class BoardNotFoundException extends NotFoundException {
    public BoardNotFoundException() {
        super(ErrorCode.BOARD_NOT_FOUND);
    }
}
