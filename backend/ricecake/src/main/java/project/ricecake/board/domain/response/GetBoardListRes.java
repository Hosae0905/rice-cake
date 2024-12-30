package project.ricecake.board.domain.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetBoardListRes {

    @NotBlank
    @Size(max = 30)
    private String boardTitle;

    @NotBlank
    @Size(max = 500)
    private String boardContent;

    @NotBlank
    @Size(min = 2, max = 20)
    private String memberName;

    public static GetBoardListRes buildBoardListRes(String boardTitle, String boardContent, String memberName) {
        return GetBoardListRes.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .memberName(memberName)
                .build();
    }
}
