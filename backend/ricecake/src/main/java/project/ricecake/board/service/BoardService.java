package project.ricecake.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.domain.response.GetBoardListRes;
import project.ricecake.board.domain.response.GetBoardRes;
import project.ricecake.board.repository.BoardRepository;
import project.ricecake.common.BaseResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Object createBoard(PostCreateBoardReq postCreateBoardReq) {
        BoardEntity board = BoardEntity.buildBoard(postCreateBoardReq);

        if (board != null) {
            boardRepository.save(board);
            return BaseResponse.successResponse("BOARD_001", true, "게시글 생성 성공", "ok");
        } else {
            return BaseResponse.failResponse("BOARD_ERROR_001", false, "게시글 생성 실패", "fail");
        }
    }

    public Object getBoardList() {
        List<BoardEntity> boards = boardRepository.findAll();

        if (!boards.isEmpty()) {
            List<GetBoardListRes> boardList = new ArrayList<>();

            for (BoardEntity board : boards) {
                boardList.add(GetBoardListRes.buildBoardListRes(board.getBoardTitle(), board.getBoardContent()));
            }

            return BaseResponse.successResponse("BOARD_002", true, "게시글 조회 성공", boardList);
        } else {
            return BaseResponse.failResponse("BOARD_ERROR_002", false, "게시글이 없습니다.", "fail");
        }
    }

    public Object getBoard(Long boardIdx) {
        Optional<BoardEntity> findBoard = boardRepository.findByBoardIdx(boardIdx);

        if (findBoard.isPresent()) {
            BoardEntity board = findBoard.get();
            GetBoardRes boardRes = GetBoardRes.buildBoardRes(board.getBoardTitle(), board.getBoardContent());
            return BaseResponse.successResponse("BOARD_003", true, "게시글 단건 조회 성공", boardRes);
        } else {
            return BaseResponse.failResponse("BOARD_ERROR_003", false, "게시글 단건 조회 실패", "fail");
        }
    }
}
