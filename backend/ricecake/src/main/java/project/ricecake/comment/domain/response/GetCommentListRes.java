package project.ricecake.comment.domain.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * GetCommentListRes
 * 클라이언트에게 댓글 목록을 반환할 응답 DTO
 */
@Builder
@Getter
public class GetCommentListRes {
    private String memberId;
    private String commentContent;
    private LocalDateTime createdAt;

    /**
     * GetCommentListRes 객체 생성
     * @param memberId          (작성자 회원 아이디)
     * @param commentContent    (댓글 내용)
     * @param createdAt         (댓글 생성 시간)
     * @return GetCommentListRes 객체
     */
    public static GetCommentListRes buildCommentListRes(String memberId, String commentContent, LocalDateTime createdAt) {
        return GetCommentListRes.builder()
                .memberId(memberId)
                .commentContent(commentContent)
                .createdAt(createdAt)
                .build();
    }
}
