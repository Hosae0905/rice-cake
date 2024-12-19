package project.ricecake.board.domain.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetBoardRes {
    private String boardTitle;
    private String boardContent;
    private String memberName;

    public static GetBoardRes buildBoardRes(String boardTitle, String boardContent, String memberName) {
        return GetBoardRes.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .memberName(memberName)
                .build();
    }
}
