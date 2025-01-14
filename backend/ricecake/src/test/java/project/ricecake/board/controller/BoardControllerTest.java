package project.ricecake.board.controller;

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
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.domain.response.GetBoardListRes;
import project.ricecake.board.domain.response.GetBoardRes;
import project.ricecake.board.service.BoardService;
import project.ricecake.common.BaseResponse;
import project.ricecake.config.WebSecurityConfig;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.service.UserDetailsServiceImpl;
import project.ricecake.utils.JwtUtils;

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
@WebMvcTest(controllers = BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @DisplayName("1. 게시글 성공 테스트")
    @Test
    void createBoardSuccess() throws Exception {
        //given
        PostCreateBoardReq postCreateBoardReq = new PostCreateBoardReq("first board", "test content", "member01");
        BaseResponse<Object> baseResponse = BaseResponse.successResponse("BOARD_001", true, "게시글 생성 성공", "ok");

        //when
        given(boardService.createBoard(any(PostCreateBoardReq.class))).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateBoardReq))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BOARD_001"))
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.message").value("게시글 생성 성공"))
                .andExpect(jsonPath("$.result").value("ok"))
                .andDo(print());
    }

    @DisplayName("1-1. 게시글 제목 유효성 검증에 실패한 경우")
    @Test
    void createFailInvalidTitle() throws Exception {
        //given
        PostCreateBoardReq postCreateBoardReq = new PostCreateBoardReq("", "test content", "member01");

        //when

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateBoardReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("게시글 제목은 공백일 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("1-2. 게시글 내용 유효성 검증에 실패한 경우")
    @Test
    void createFailInvalidContent() throws Exception {
        //given
        PostCreateBoardReq postCreateBoardReq = new PostCreateBoardReq("first board", "", "member01");

        //when

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateBoardReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("게시글은 공백일 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("1-3. 작성자 아이디 유효성 검증에 실패한 경우")
    @Test
    void createFailInvalidMemberId() throws Exception {
        //given
        PostCreateBoardReq postCreateBoardReq = new PostCreateBoardReq("first board", "test content", "test");

        //when

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateBoardReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("아이디 형식이 잘못되었습니다."))
                .andDo(print());
    }

    @DisplayName("1-4. 작성자 정보가 없는 경우")
    @Test
    void createFailNotFoundMember() throws Exception {
        //given
        PostCreateBoardReq postCreateBoardReq = new PostCreateBoardReq("first board", "test content", "tester01");

        //when
        given(boardService.createBoard(any(PostCreateBoardReq.class))).willThrow(new UserNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/board/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateBoardReq))
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("MEMBER_E001"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("2. 게시글 전체 조회 성공 테스트")
    @Test
    void getBoardListSuccess() throws Exception {

        //given
        int page = 1;
        int size = 10;
        List<GetBoardListRes> boardList = new ArrayList<>();
        GetBoardListRes boardListRes1 = GetBoardListRes.buildBoardListRes("first board", "test content", "member01");
        GetBoardListRes boardListRes2 = GetBoardListRes.buildBoardListRes("second board", "test content", "member02");

        boardList.add(boardListRes1);
        boardList.add(boardListRes2);

        BaseResponse<Object> baseResponse = BaseResponse.successResponse(
                "BOARD_002", true, "게시글 조회 성공", objectMapper.writeValueAsString(boardList));

        //when
        given(boardService.getBoardList(page, size)).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/board")
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BOARD_002"))
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.message").value("게시글 조회 성공"))
                .andExpect(jsonPath("$.result").value(objectMapper.writeValueAsString(boardList)))
                .andDo(print());
    }

    @DisplayName("2-1. 게시글 전체 조회 시 특정 게시글이 없는 경우")
    @Test
    void getBoardListFailNotFoundBoard() throws Exception {

        //given
        int page = 1;
        int size = 10;

        //when
        given(boardService.getBoardList(page, size)).willThrow(new BoardNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/board")
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("BOARD_E001"))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
                .andDo(print());
    }


    @DisplayName("3. 게시글 단건 조회 성공 테스트")
    @Test
    void getBoardSuccess() throws Exception {

        //given
        GetBoardRes boardRes = GetBoardRes.buildBoardRes("first board", "test content", "member01");
        BaseResponse<Object> baseResponse = BaseResponse.successResponse(
                "BOARD_003", true, "게시글 단건 조회 성공", objectMapper.writeValueAsString(boardRes));

        Long pathVariable = 1L;

        //when
        given(boardService.getBoard(pathVariable)).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/board/" + pathVariable)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("BOARD_003"))
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.message").value("게시글 단건 조회 성공"))
                .andExpect(jsonPath("$.result").value(objectMapper.writeValueAsString(boardRes)))
                .andDo(print());
    }

    @DisplayName("3-1. 특정 게시글이 없는 경우")
    @Test
    void getBoardFailNotFoundBoard() throws Exception {

        //given
        Long pathVariable = 99L;

        //when
        given(boardService.getBoard(pathVariable)).willThrow(new BoardNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/board/" + pathVariable)
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("BOARD_E001"))
                .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("3-2. 게시글 아이디 타입이 안 맞을 경우")
    @Test
    void getBoardFailTypeMismatch() throws Exception {
        //given
        String pathVariable = "test";

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/board/" + pathVariable)
        );

        //then
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.code").value("COMMON_E002"))
                .andExpect(jsonPath("$.message").value("서버에서 오류가 발생하였습니다."))
                .andDo(print());
    }
}
