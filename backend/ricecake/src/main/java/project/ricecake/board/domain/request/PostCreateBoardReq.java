package project.ricecake.board.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PostCreateBoardReq
 * 클라이언트의 게시글 생성 요청 정보를 담을 DTO 클래스
 */
@AllArgsConstructor
@Getter
public class PostCreateBoardReq {

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    @Size(max = 30, message = "게시글 제목은 최대 30자까지 가능합니다.")
    private String boardTitle;

    @NotBlank(message = "게시글은 공백일 수 없습니다.")
    @Size(max = 500, message = "게시글 내용은 최대 500자까지 가능합니다.")
    private String boardContent;
}
