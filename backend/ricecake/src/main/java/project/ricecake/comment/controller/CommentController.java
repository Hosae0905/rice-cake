package project.ricecake.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @RequestMapping(method = RequestMethod.POST, value = "/write")
    public ResponseEntity<Object> writeComment(@RequestBody PostWriteCommentReq postWriteCommentReq) {
        return ResponseEntity.ok().body(commentService.writeComment(postWriteCommentReq));
    }

    // TODO: 댓글 수정 기능 개발하기.


    // TODO: 댓글 삭제 기능 개발하기.
}
