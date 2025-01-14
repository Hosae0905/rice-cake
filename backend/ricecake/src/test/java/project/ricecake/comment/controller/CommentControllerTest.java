package project.ricecake.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.comment.domain.response.GetCommentListRes;
import project.ricecake.comment.service.CommentService;
import project.ricecake.common.BaseResponse;
import project.ricecake.config.WebSecurityConfig;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.service.UserDetailsServiceImpl;
import project.ricecake.utils.JwtUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Import({WebSecurityConfig.class})
@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

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
        PostWriteCommentReq postWriteCommentReq = new PostWriteCommentReq("test comment", null, 1L);

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
                .andExpect(jsonPath("$.message").value("회원 아이디는 공백일 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("1-3. 작성자를 찾을 수 없는 경우")
    @Test
    void writeFailNotFoundMember() throws Exception {

        //given
        PostWriteCommentReq postWriteCommentReq = new PostWriteCommentReq("test comment", "tester01", 1L);

        //when
        given(commentService.writeComment(any(PostWriteCommentReq.class))).willThrow(new UserNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/comment/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postWriteCommentReq))
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("MEMBER_E001"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("1-4. 게시글이 없는 경우")
    @Test
    void writeFailNotFoundBoard() throws Exception {
        //given
        PostWriteCommentReq postWriteCommentReq = new PostWriteCommentReq("test comment", "tester01", 100L);

        //when
        given(commentService.writeComment(any(PostWriteCommentReq.class))).willThrow(new BoardNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/comment/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postWriteCommentReq))
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("BOARD_E001"))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("2. 댓글 목록 조회 성공")
    @Test
    void getCommentListSuccess() throws Exception {
        //given
        int page = 1;
        int size = 10;
        Long boardIdx = 1L;

        GetCommentListRes getCommentListRes1 = GetCommentListRes.buildCommentListRes("member01", "first comment", LocalDateTime.now());
        GetCommentListRes getCommentListRes2 = GetCommentListRes.buildCommentListRes("member02", "second comment", LocalDateTime.now());

        List<GetCommentListRes> commentList = new ArrayList<>();
        commentList.add(getCommentListRes1);
        commentList.add(getCommentListRes2);

        BaseResponse<Object> baseResponse = BaseResponse.successResponse("COMMENT_002", true, "댓글 조회 성공", objectMapper.writeValueAsString(commentList));

        //when
        given(commentService.getCommentList(page, size, boardIdx)).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/comment/" + boardIdx)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMENT_002"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("댓글 조회 성공"))
                .andExpect(jsonPath("$.result").value(objectMapper.writeValueAsString(commentList)))
                .andDo(print());
    }

    @DisplayName("2-1. 댓글 목록 조회 시 특정 게시글이 없을 경우")
    @Test
    void getCommentListFailNotFoundBoard() throws Exception {
        //given
        int page = 1;
        int size = 10;
        Long boardIdx = 99L;

        //when
        given(commentService.getCommentList(page, size, boardIdx)).willThrow(new BoardNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/comment/" + boardIdx)
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("BOARD_E001"))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
                .andDo(print());
    }
}
