package project.ricecake.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.board.repository.BoardRepository;
import project.ricecake.comment.domain.entity.CommentEntity;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.domain.response.GetCommentListRes;
import project.ricecake.comment.repository.CommentRepository;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
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

    public Object getCommentList(int page, int size, Long boardIdx) {
        Optional<BoardEntity> findBoard = boardRepository.findByBoardIdx(boardIdx);
        List<GetCommentListRes> commentList = new ArrayList<>();

        Pageable pageable = PageRequest.of(page - 1, size);

        if (findBoard.isPresent()) {
            BoardEntity board = findBoard.get();
            Page<CommentEntity> comments = commentRepository.findAllByBoard(pageable, board);

            for (CommentEntity comment : comments) {
                commentList.add(GetCommentListRes.buildCommentListRes(
                        comment.getMember().getMemberId(),
                        comment.getCommentContent(),
                        comment.getStartedAt()));
            }

        } else {
            throw new BoardNotFoundException();
        }

        return BaseResponse.successResponse("COMMENT_002", true, "댓글 조회 성공", commentList);
    }
}
