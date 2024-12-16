package project.ricecake.board.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCreateBoardReq {
    private String boardTitle;
    private String boardContent;
}
