package project.ricecake.comment.domain.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetCommentListRes {
    private String memberId;
    private String commentContent;
    private LocalDateTime createdAt;

    public static GetCommentListRes buildCommentListRes(String memberId, String commentContent, LocalDateTime createdAt) {
        return GetCommentListRes.builder()
                .memberId(memberId)
                .commentContent(commentContent)
                .createdAt(createdAt)
                .build();
    }
}
