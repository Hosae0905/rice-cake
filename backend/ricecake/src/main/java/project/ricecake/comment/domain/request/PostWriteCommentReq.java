package project.ricecake.comment.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostWriteCommentReq {
    private String commentContent;
    private String memberId;
    private Long boardIdx;
}
