package project.ricecake.board.domain.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetBoardListRes {
    private String boardTitle;
    private String boardContent;
    private String memberName;

    public static GetBoardListRes buildBoardListRes(String boardTitle, String boardContent, String memberName) {
        return GetBoardListRes.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .memberName(memberName)
                .build();
    }
}
