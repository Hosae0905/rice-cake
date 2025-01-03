package project.ricecake.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.board.repository.BoardRepository;
import project.ricecake.comment.domain.entity.CommentEntity;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.repository.CommentRepository;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Object writeComment(PostWriteCommentReq postWriteCommentReq) {

        MemberEntity member = null;
        BoardEntity board = null;

        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postWriteCommentReq.getMemberId());

        if (findMember.isPresent()) {
            member = findMember.get();
        } else {
            throw new UserNotFoundException();
        }

        Optional<BoardEntity> findBoard = boardRepository.findByBoardIdx(postWriteCommentReq.getBoardIdx());

        if (findBoard.isPresent()) {
            board = findBoard.get();
        } else {
            throw new BoardNotFoundException();
        }

        CommentEntity comment = CommentEntity.buildComment(postWriteCommentReq, member, board);
        commentRepository.save(comment);
        return BaseResponse.successResponse("COMMENT_001", true, "댓글 작성 성공", "ok");
    }
}
