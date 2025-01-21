package project.ricecake.comment.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PostWriteCommentReq
 * 클라이언트의 댓글 작성 요청 정보를 담을 DTO 클래스
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostWriteCommentReq {

    @NotBlank(message = "댓글 내용은 공백일 수 없습니다.")
    @Size(max = 200, message = "댓글 내용은 최대 200자까지 작성할 수 있습니다.")
    private String commentContent;

    @NotNull(message = "게시글 번호는 필수입니다.")
    private Long boardIdx;
}
