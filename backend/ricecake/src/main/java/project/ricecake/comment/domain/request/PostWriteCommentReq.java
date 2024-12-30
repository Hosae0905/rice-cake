package project.ricecake.comment.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostWriteCommentReq {

    @NotBlank
    @Size(max = 200)
    private String commentContent;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{8,20}$")
    private String memberId;

    @NotBlank
    private Long boardIdx;
}
