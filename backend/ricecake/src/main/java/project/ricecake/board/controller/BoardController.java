package project.ricecake.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.service.BoardService;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<Object> createBoard(@RequestBody @Valid PostCreateBoardReq postCreateBoardReq) {
        return ResponseEntity.ok().body(boardService.createBoard(postCreateBoardReq));
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<Object> getBoardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(boardService.getBoardList(page, size));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{boardIdx}")
    public ResponseEntity<Object> getBoard(@PathVariable Long boardIdx) {
        return ResponseEntity.ok().body(boardService.getBoard(boardIdx));
    }

    // TODO: 게시글 수정 기능 개발하기

    // TODO: 게시글 삭제 기능 개발하기
}
