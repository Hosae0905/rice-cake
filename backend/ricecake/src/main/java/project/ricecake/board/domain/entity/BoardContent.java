package project.ricecake.board.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * BoardContent
 * DB에 저장할 게시글 정보를 담을 클래스
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardContent {

    @Id
    @Column(name = "board_idx", nullable = false)
    private Long boardIdx;

    @Lob
    @Column(name = "board_content", nullable = false, length = 256)
    private String boardContent;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "board_Idx")
    private BoardEntity board;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * DB에 저장할 BoardContent 객체를 생성
     * @param boardContent  (게시글 내용)
     * @param board         (연관관계 주입을 위한 게시글 객체)
     * @return BoardContent 객체
     */
    public static BoardContent buildBoardContent(String boardContent, BoardEntity board) {
        return BoardContent.builder()
                .boardContent(boardContent)
                .board(board)
                .build();
    }
}
