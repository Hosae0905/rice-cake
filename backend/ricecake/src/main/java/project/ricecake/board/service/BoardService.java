package project.ricecake.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.board.domain.request.PostCreateBoardReq;
import project.ricecake.board.domain.response.GetBoardListRes;
import project.ricecake.board.domain.response.GetBoardRes;
import project.ricecake.board.repository.BoardRepository;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.notfound.BoardNotFoundException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BaseResponse<Object> createBoard(PostCreateBoardReq postCreateBoardReq) {
        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postCreateBoardReq.getMemberId());
        if (findMember.isPresent()) {
            MemberEntity member = findMember.get();
            BoardEntity board = BoardEntity.buildBoard(postCreateBoardReq, member);
            boardRepository.save(board);
            return BaseResponse.successResponse("BOARD_001", true, "게시글 생성 성공", "ok");
        } else {
            throw new UserNotFoundException();
        }
    }

    public BaseResponse<Object> getBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<BoardEntity> boards = boardRepository.findAll(pageable);
        if (!boards.isEmpty()) {
            List<GetBoardListRes> boardList = new ArrayList<>();

            for (BoardEntity board : boards) {
                boardList.add(GetBoardListRes.buildBoardListRes(board.getBoardTitle(), board.getBoardContent(), board.getMember().getMemberName()));
            }

            return BaseResponse.successResponse("BOARD_002", true, "게시글 조회 성공", boardList);
        } else {
            throw new BoardNotFoundException();
        }
    }

    public BaseResponse<Object> getBoard(Long boardIdx) {
        Optional<BoardEntity> findBoard = boardRepository.findByBoardIdx(boardIdx);

        if (findBoard.isPresent()) {
            BoardEntity board = findBoard.get();
            GetBoardRes boardRes = GetBoardRes.buildBoardRes(board.getBoardTitle(), board.getBoardContent(), board.getMember().getMemberName());
            return BaseResponse.successResponse("BOARD_003", true, "게시글 단건 조회 성공", boardRes);
        } else {
            throw new BoardNotFoundException();
        }
    }
}
