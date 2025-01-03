package project.ricecake.board.domain.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetBoardRes {

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    @Size(max = 30, message = "게시글 제목은 최대 30자까지 가능합니다.")
    private String boardTitle;

    @NotBlank(message = "게시글은 공백일 수 없습니다.")
    @Size(max = 500, message = "게시글 내용은 최대 500자까지 가능합니다.")
    private String boardContent;

    @NotBlank(message = "회원 아이디는 공백일 수 없습니다.")
    @Size(min = 2, max = 20, message = "회원의 이름은 최소 2자부터 최대 20자까지 가능합니다.")
    private String memberName;

    public static GetBoardRes buildBoardRes(String boardTitle, String boardContent, String memberName) {
        return GetBoardRes.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .memberName(memberName)
                .build();
    }
}
