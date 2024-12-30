package project.ricecake.board.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCreateBoardReq {

    @NotBlank
    @Size(max = 30)
    private String boardTitle;

    @NotBlank
    @Size(max = 500)
    private String boardContent;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{8,20}$")
    private String memberId;
}
