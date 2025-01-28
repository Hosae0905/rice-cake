package project.ricecake.board.service;

import org.springframework.stereotype.Service;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.domain.response.GetBoardListRes;
import project.ricecake.board.domain.response.GetBoardRes;
import project.ricecake.common.BaseResponse;

import java.util.List;

/**
 * BoardService 인터페이스
 * 게시글과 관련된 기능들을 모아놓은 인터페이스
 */
@Service
public interface BoardService {
    /**
     * 게시글 생성 기능
     * @param memberId           (회원 아이디)
     * @param postCreateBoardReq (게시글 생성 요청 DTO)
     * @return 게시글 생성 성공 응답 객체(Object)
     */
    BaseResponse<String> createBoard(String memberId, PostCreateBoardReq postCreateBoardReq);

    /**
     * 게시글 목록 조회 기능
     * @param page (페이지 번호)
     * @param size (페이지 크기)
     * @return 게시글 목록 조회 성공 응답(Object)
     */
    BaseResponse<List<GetBoardListRes>> getBoardList(int page, int size);

    /**
     * 게시글 단건 조회 기능
     * @param boardIdx (게시글 인덱스)
     * @return 게시글 단건 조회 성공 응답(GetBoardRes)
     */
    BaseResponse<GetBoardRes> getBoard(Long boardIdx);
}
