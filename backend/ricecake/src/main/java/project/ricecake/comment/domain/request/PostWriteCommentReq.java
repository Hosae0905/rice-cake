package project.ricecake.comment.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostWriteCommentReq {

    @NotBlank(message = "댓글 내용은 공백일 수 없습니다.")
    @Size(max = 200, message = "댓글 내용은 최대 200자까지 작성할 수 있습니다.")
    private String commentContent;

    @NotBlank(message = "회원 아이디는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9]{8,20}$", message = "아이디 형식이 잘못되었습니다.")
    private String memberId;

    @NotNull(message = "게시글 번호는 필수입니다.")
    private Long boardIdx;
}
