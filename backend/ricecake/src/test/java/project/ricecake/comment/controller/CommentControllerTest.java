package project.ricecake.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("1. 댓글 작성 성공 테스트")
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

    @DisplayName("1-1. 댓글 내용 유효성 검증에 실패한 경우")
    @Test
    void writeFailInvalidComment() throws Exception {

        //given
        PostWriteCommentReq postWriteCommentReq = new PostWriteCommentReq("", "member01", 1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/comment/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postWriteCommentReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("댓글 내용은 공백일 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("1-2. 작성자 아이디 유효성 검증에 실패한 경우")
    @Test
    void writeFailInvalidMemberId() throws Exception {

        //given
        PostWriteCommentReq postWriteCommentReq = new PostWriteCommentReq("test comment", "", 1L);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/comment/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postWriteCommentReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("아이디 형식이 잘못되었습니다."))
                .andDo(print());
    }
    
    //TODO: 작성자를 찾을 수 없는 경우
    
    //TODO: 게시글이 없는 경우
    
    //TODO: 게시글 아이디의 타입이 안 맞는 경우
}
