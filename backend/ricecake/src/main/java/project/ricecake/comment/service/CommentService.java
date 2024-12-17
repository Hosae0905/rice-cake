package project.ricecake.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.comment.domain.entity.CommentEntity;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.repository.CommentRepository;
import project.ricecake.common.BaseResponse;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Object writeComment(PostWriteCommentReq postWriteCommentReq) {
        if (postWriteCommentReq.getCommentContent() != null) {
            CommentEntity comment = CommentEntity.buildComment(postWriteCommentReq);
            commentRepository.save(comment);
            return BaseResponse.successResponse("COMMENT_001", true, "댓글 작성 성공", "ok");
        } else {
            return BaseResponse.failResponse("COMMENT_ERROR_001", false, "댓글 작성 실패", "fail");
        }
    }
}
