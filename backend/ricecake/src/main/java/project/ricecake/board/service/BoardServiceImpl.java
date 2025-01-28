package project.ricecake.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ricecake.board.domain.entity.BoardContent;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.domain.response.GetBoardListRes;
import project.ricecake.board.domain.response.GetBoardRes;
import project.ricecake.board.repository.BoardContentRepository;
import project.ricecake.board.repository.BoardRepository;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.List;

/**
 * BoardServiceImpl
 * 게시글에 관련된 기능들의 서비스 로직을 처리하기 위한 클래스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardContentRepository boardContentRepository;

    /**
     * 게시글 생성 서비스 로직
     * @param memberId           (회원 아이디)
     * @param postCreateBoardReq (게시글 생성 요청 DTO)
     * @return 게시글 생성 성공 응답 객체(String)
     * @throws UserNotFoundException : 작성자 회원 정보를 찾을 수 없을 경우
     */
    @Transactional
    public BaseResponse<String> createBoard(String memberId, PostCreateBoardReq postCreateBoardReq) {

        /**
         * 만약 작성자가 있다면 작성자 정보와 게시글 생성 요청 정보를 통해 게시글을 만들어 DB에 저장하고 성공 응답을 반환
         * 그렇지 않을 경우 UserNotFoundException 예외를 throw
         */

        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow(UserNotFoundException::new);
        BoardEntity board = BoardEntity.buildBoard(postCreateBoardReq, member);
        BoardEntity saveBoard = boardRepository.save(board);
        BoardContent boardContent = BoardContent.buildBoardContent(
                postCreateBoardReq.getBoardContent(),
                saveBoard);
        boardContentRepository.save(boardContent);
        return BaseResponse.successResponse("BOARD_001", true, "게시글 생성 성공", "ok");
    }

    /**
     * 게시글 목록 조회 서비스 로직
     * @param page (페이지 번호)
     * @param size (페이지 크기)
     * @return 게시글 목록 조회 성공 응답(List<GetBoardListRes>)
     * @throws BoardNotFoundException : 게시글을 찾을 수 없을 경우
     */
    public BaseResponse<List<GetBoardListRes>> getBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardEntity> boards = boardRepository.findAll(pageable);

        /**
         * 만약 게시글이 있으면 게시글 목록을 List에 담아서 성공 응답으로 반환
         * 그렇지 않다면 BoardNotFoundException 예외를 throw
         */

        if (boards.isEmpty()) {
            throw new BoardNotFoundException();
        }

        List<GetBoardListRes> boardList = boards.stream()
                .map(board ->
                    GetBoardListRes.buildBoardListRes(
                            board.getBoardTitle(),
                            board.getMember().getMemberName(),
                            board.getStartedAt()
                    ))
                .toList();

        return BaseResponse.successResponse("BOARD_002", true, "게시글 조회 성공", boardList);
    }

    /**
     * 게시글 단건 조회 서비스 로직
     * @param boardIdx (게시글 인덱스)
     * @return 게시글 단건 조회 성공 응답(GetBoardRes)
     * @throws BoardNotFoundException : 게시글을 찾을 수 없을 경우
     */
    public BaseResponse<GetBoardRes> getBoard(Long boardIdx) {

        /**
         * 만약 게시글이 있다면 게시글 정보를 변수에 저장
         * 그렇지 않다면 BoardNotFoundException 예외를 throw
         */
        BoardEntity board = boardRepository.findByBoardIdx(boardIdx).orElseThrow(BoardNotFoundException::new);

        /**
         * 게시글 정보를 통해 게시글 내용을 변수에 저장
         * 게시글 정보와 내용을 포함한 응답 DTO를 생성하고 클라이언트에게 반환
         */
        BoardContent boardContent = board.getBoardContent();
        GetBoardRes boardRes = GetBoardRes.buildBoardRes(
                board.getBoardTitle(),
                boardContent.getBoardContent(),
                board.getMember().getMemberName(),
                boardContent.getCreatedAt());

        return BaseResponse.successResponse("BOARD_003", true, "게시글 단건 조회 성공", boardRes);
    }
}
