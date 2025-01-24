package project.ricecake.board.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * GetBoardListRes
 * 클라이언트에게 게시글 목록을 반환할 응답 DTO
 */
@Builder
@Getter
public class GetBoardListRes {

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    @Size(max = 30, message = "게시글 제목은 최대 30자까지 가능합니다.")
    private String boardTitle;

    @NotBlank(message = "회원 아이디는 공백일 수 없습니다.")
    @Size(min = 2, max = 20, message = "회원의 이름은 최소 2자부터 최대 20자까지 가능합니다.")
    private String memberName;

    @NotBlank(message = "게시글 생성 시간은 공백일 수 없습니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startedAt;

    /**
     * GetBoardListRes 객체 생성
     * @param boardTitle (게시글 제목)
     * @param memberName (작성자 회원 이름)
     * @return GetBoardListRes 객체
     */
    public static GetBoardListRes buildBoardListRes(String boardTitle, String memberName, LocalDateTime startedAt) {
        return GetBoardListRes.builder()
                .boardTitle(boardTitle)
                .memberName(memberName)
                .startedAt(startedAt)
                .build();
    }
}
