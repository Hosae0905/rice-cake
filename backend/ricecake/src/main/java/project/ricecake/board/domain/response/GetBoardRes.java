package project.ricecake.board.domain.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetBoardRes {
    private String boardTitle;
    private String boardContent;

    public static GetBoardRes buildBoardRes(String boardTitle, String boardContent) {
        return GetBoardRes.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .build();
    }
}
