package project.ricecake.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @RequestMapping(method = RequestMethod.POST, value = "/write")
    public ResponseEntity<Object> writeComment(@RequestBody @Valid PostWriteCommentReq postWriteCommentReq) {
        return ResponseEntity.ok().body(commentService.writeComment(postWriteCommentReq));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{boardIdx}")
    public ResponseEntity<Object> writeComment(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long boardIdx
    ) {
        return ResponseEntity.ok().body(commentService.getCommentList(page, size, boardIdx));
    }



    // TODO: 댓글 수정 기능 개발하기.


    // TODO: 댓글 삭제 기능 개발하기.
}
