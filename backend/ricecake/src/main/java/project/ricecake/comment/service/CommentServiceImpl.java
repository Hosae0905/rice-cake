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

import java.util.List;

/**
 * CommentService
 * 게시글에 관련된 기능들의 서비스 로직을 처리하기 위한 클래스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    /**
     * 댓글 작성 서비스 로직
     * @param postWriteCommentReq (댓글 작성 요청 DTO)
     * @return 댓글 작성 성공 응답 객체(String)
     * @throws UserNotFoundException : 작성자 회원 정보를 찾을 수 없을 경우
     * @throws BoardNotFoundException : 댓글을 작성할 게시글 정보를 찾을 수 없을 경우
     */
    @Transactional
    public BaseResponse<String> writeComment(String memberId, PostWriteCommentReq postWriteCommentReq) {

        /**
         * 만약 회원이 있다면 변수에 회원 정보를 저장
         * 그렇지 않다면 UserNotFoundException 예외를 throws
         */
        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow(UserNotFoundException::new);

        /**
         * 만약 게시글이 있다면 변수에 게시글 정보를 저장
         * 그렇지 않다면 BoardNotFoundException 예외를 throws
         */
        BoardEntity board = boardRepository.findByBoardIdx(postWriteCommentReq.getBoardIdx()).orElseThrow(BoardNotFoundException::new);

        // Comment 엔티티 생성 후 DB에 저장
        CommentEntity comment = CommentEntity.buildComment(postWriteCommentReq, member, board);
        commentRepository.save(comment);

        return BaseResponse.successResponse("COMMENT_001", true, "댓글 작성 성공", "ok");
    }

    /**
     * 댓글 목록 조회 서비스 로직
     * @param page      (페이지 번호)
     * @param size      (페이지 사이즈)
     * @param boardIdx  (게시글 인덱스)
     * @return 댓글 목록 조회 성공 응답(List<GetCommentListRes>)
     * @throws BoardNotFoundException : 댓글을 작성할 게시글을 찾을 수 없을 경우
     */
    public BaseResponse<List<GetCommentListRes>> getCommentList(int page, int size, Long boardIdx) {

        /**
         * 만약 게시글이 있다면 게시글 정보와 페이지 정보를 통해 게시글 목록을 조회
         * 그렇지 않다면 BoardNotFoundException 예외를 throw
         */
        BoardEntity board = boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommentEntity> comments = commentRepository.findAllByBoard(pageable, board);

        /**
         * 페이지 단위로 조회된 게시글 정보를 응답 DTO로 생성하여 리스트에 저장
         */

        List<GetCommentListRes> commentList = comments.stream()
                .map(comment ->
                    GetCommentListRes.buildCommentListRes(
                            comment.getMember().getMemberId(),
                            comment.getCommentContent(),
                            comment.getStartedAt()
                    ))
                .toList();

        return BaseResponse.successResponse("COMMENT_002", true, "댓글 조회 성공", commentList);
    }
}
