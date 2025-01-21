package project.ricecake.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.domain.response.GetBoardListRes;
import project.ricecake.board.domain.response.GetBoardRes;
import project.ricecake.board.repository.BoardRepository;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BoardService
 * 게시글에 관련된 기능들의 서비스 로직을 처리하기 위한 클래스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 생성 서비스 로직
     * @param postCreateBoardReq (게시글 생성 요청 DTO)
     * @return 게시글 생성 성공 응답 객체(String)
     * @throws UserNotFoundException : 작성자 회원 정보를 찾을 수 없을 경우
     */
    @Transactional
    public BaseResponse<Object> createBoard(String memberId, PostCreateBoardReq postCreateBoardReq) {

        // 인증된 회원의 아이디를 활용해서 DB에서 회원을 찾기
        Optional<MemberEntity> findMember = memberRepository.findByMemberId(memberId);

        /**
         * 만약 작성자가 있다면 작성자 정보와 게시글 생성 요청 정보를 통해 게시글을 만들어 DB에 저장하고 성공 응답을 반환
         * 그렇지 않을 경우 UserNotFoundException 예외를 throw
         */
        if (findMember.isPresent()) {
            MemberEntity member = findMember.get();
            BoardEntity board = BoardEntity.buildBoard(postCreateBoardReq, member);
            boardRepository.save(board);
            return BaseResponse.successResponse("BOARD_001", true, "게시글 생성 성공", "ok");
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * 게시글 목록 조회 서비스 로직
     * @param page (페이지 번호)
     * @param size (페이지 크기)
     * @return 게시글 목록 조회 성공 응답(List<GetBoardListRes>)
     * @throws BoardNotFoundException : 게시글을 찾을 수 없을 경우
     */
    public BaseResponse<Object> getBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardEntity> boards = boardRepository.findAll(pageable);

        /**
         * 만약 게시글이 있으면 게시글 목록을 List에 담아서 성공 응답으로 반환
         * 그렇지 않다면 BoardNotFoundException 예외를 throw
         */
        if (!boards.isEmpty()) {
            List<GetBoardListRes> boardList = new ArrayList<>();

            for (BoardEntity board : boards) {
                boardList.add(GetBoardListRes.buildBoardListRes(board.getBoardTitle(), board.getBoardContent(), board.getMember().getMemberName()));
            }

            return BaseResponse.successResponse("BOARD_002", true, "게시글 조회 성공", boardList);
        } else {
            throw new BoardNotFoundException();
        }
    }

    /**
     * 게시글 단건 조회 서비스 로직
     * @param boardIdx (게시글 인덱스)
     * @return 게시글 단건 조회 성공 응답(GetBoardRes)
     * @throws BoardNotFoundException : 게시글을 찾을 수 없을 경우
     */
    public BaseResponse<Object> getBoard(Long boardIdx) {
        Optional<BoardEntity> findBoard = boardRepository.findByBoardIdx(boardIdx);

        /**
         * 만약 게시글이 있다면 게시글 정보를 통해 응답 DTO를 생성하여 성공 응답을 반환
         * 그렇지 않다면 BoardNotFoundException 예외를 throw
         */
        if (findBoard.isPresent()) {
            BoardEntity board = findBoard.get();
            GetBoardRes boardRes = GetBoardRes.buildBoardRes(board.getBoardTitle(), board.getBoardContent(), board.getMember().getMemberName());
            return BaseResponse.successResponse("BOARD_003", true, "게시글 단건 조회 성공", boardRes);
        } else {
            throw new BoardNotFoundException();
        }
    }
}
