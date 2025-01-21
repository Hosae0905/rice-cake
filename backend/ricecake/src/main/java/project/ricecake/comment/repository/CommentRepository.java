package project.ricecake.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.comment.domain.entity.CommentEntity;

/**
 * CommentRepository
 * DB에 접근하여 관련 쿼리 작업을 진행하는 인터페이스
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    /**
     * 댓글 목록을 페이지 단위로 조회하는 기능
     * @param pageable (페이지 정보)
     * @param board    (게시글 정보)
     * @return 페이징 처리가 된 CommentEntity 객체
     */
    Page<CommentEntity> findAllByBoard(Pageable pageable, BoardEntity board);
}
