package project.ricecake.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.service.CommentService;
import project.ricecake.common.BaseResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void writeCommentSuccess() throws Exception {

        //given
        PostWriteCommentReq postWriteCommentReq = new PostWriteCommentReq("test comment", "member01", 1L);
        BaseResponse<Object> baseResponse = BaseResponse.successResponse("COMMENT_001", true, "댓글 작성 성공", "ok");

        //when
        given(commentService.writeComment(any(PostWriteCommentReq.class))).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/comment/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postWriteCommentReq))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMENT_001"))
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.message").value("댓글 작성 성공"))
                .andExpect(jsonPath("$.result").value("ok"))
                .andDo(print());
    }
}
