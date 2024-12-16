package project.ricecake.board.domain.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetBoardListRes {
    private String boardTitle;
    private String boardContent;

    public static GetBoardListRes buildBoardListRes(String boardTitle, String boardContent) {
        return GetBoardListRes.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .build();
    }
}
