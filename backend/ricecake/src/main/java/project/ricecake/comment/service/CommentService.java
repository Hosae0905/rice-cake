package project.ricecake.comment.service;

import org.springframework.stereotype.Service;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.domain.response.GetCommentListRes;
import project.ricecake.common.BaseResponse;

import java.util.List;

@Service
public interface CommentService {
    public BaseResponse<String> writeComment(String memberId, PostWriteCommentReq postWriteCommentReq);
    public BaseResponse<List<GetCommentListRes>> getCommentList(int page, int size, Long boardIdx);
}
