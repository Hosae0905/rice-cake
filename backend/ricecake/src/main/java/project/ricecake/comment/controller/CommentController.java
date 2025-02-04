package project.ricecake.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.domain.response.GetCommentListRes;
import project.ricecake.comment.service.CommentService;
import project.ricecake.common.BaseResponse;

import java.util.List;

/**
 * CommentController - v1
 * 클라이언트의 댓글에 관한 요청을 받는 컨트롤러 클래스
 * 기능 목록: 댓글 작성, 댓글 목록 조회
 */
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 작성 API
     * @param memberId            (인증된 회원 아이디)
     * @param postWriteCommentReq (댓글 작성 요청 DTO)
     * @return 댓글 작성 성공 응답
     */
    @PostMapping("/write")
    public ResponseEntity<BaseResponse<String>> writeComment(
            @AuthenticationPrincipal String memberId,
            @RequestBody @Valid PostWriteCommentReq postWriteCommentReq
    ) {
        return ResponseEntity.ok().body(commentService.writeComment(memberId, postWriteCommentReq));
    }

    /**
     * 댓글 목록 조회 API
     * @param page      (페이지 번호, 기본 값 1)
     * @param size      (페이지 사이즈, 기본 값 10)
     * @param boardIdx  (게시글 인덱스)
     * @return 댓글 목록 조회 성공 응답
     */
    @GetMapping("{boardIdx}")
    public ResponseEntity<BaseResponse<List<GetCommentListRes>>> getCommentList(
            @PathVariable Long boardIdx,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(commentService.getCommentList(page, size, boardIdx));
    }



    // TODO: 댓글 수정 기능 개발하기.


    // TODO: 댓글 삭제 기능 개발하기.
}
