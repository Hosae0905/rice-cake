package project.ricecake.board.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCreateBoardReq {

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    @Size(max = 30, message = "게시글 제목은 최대 30자까지 가능합니다.")
    private String boardTitle;

    @NotBlank(message = "게시글은 공백일 수 없습니다.")
    @Size(max = 500, message = "게시글 내용은 최대 500자까지 가능합니다.")
    private String boardContent;

    @NotBlank(message = "회원 아이디는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9]{8,20}$", message = "아이디 형식이 잘못되었습니다.")
    private String memberId;
}
