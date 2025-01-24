package project.ricecake.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.service.BoardService;
import project.ricecake.common.BaseResponse;

/**
 * BoardController - v1
 * 클라이언트의 게시글에 관한 요청을 받는 컨트롤러 클래스
 * 기능 목록: 게시글 작성, 게시글 전체 조회, 게시글 단건 조회
 */
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    /**
     * 게시글 작성 API
     * @param memberId           (인증된 회원 아이디)
     * @param postCreateBoardReq (게시글 생성 요청 DTO)
     * @return 게시글 생성 성공 응답
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<BaseResponse<Object>> createBoard(@AuthenticationPrincipal String memberId, @RequestBody @Valid PostCreateBoardReq postCreateBoardReq) {
        return ResponseEntity.ok().body(boardService.createBoard(memberId, postCreateBoardReq));
    }

    /**
     * 게시글 목록 조회 API
     * @param page (페이지 번호, 기본 값 1)
     * @param size (페이지 사이즈, 기본 값 10)
     * @return 게시글 목록 조회 성공 응답
     */
    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<BaseResponse<Object>> getBoardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(boardService.getBoardList(page, size));
    }

    /**
     * 게시글 단건 조회 API
     * @param boardIdx (게시글 인덱스)
     * @return 게시글 단건 조회 성공 응답
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{boardIdx}")
    public ResponseEntity<BaseResponse<Object>> getBoard(@PathVariable Long boardIdx) {
        return ResponseEntity.ok().body(boardService.getBoard(boardIdx));
    }

    // TODO: 게시글 수정 기능 개발하기

    // TODO: 게시글 삭제 기능 개발하기
}
