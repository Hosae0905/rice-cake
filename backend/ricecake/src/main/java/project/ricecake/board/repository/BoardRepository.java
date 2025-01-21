package project.ricecake.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ricecake.board.domain.entity.BoardEntity;

import java.util.Optional;

/**
 * BoardRepository
 * DB에 접근하여 관련 쿼리 작업을 진행하는 인터페이스
 */
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /**
     * 게시글 인덱스를 통해 게시글을 찾는 기능
     * @param boardIdx (게시글 인덱스)
     * @return Optional로 감싸진 BoardEntity 객체
     */
    Optional<BoardEntity> findByBoardIdx(Long boardIdx);

    /**
     * 게시글 목록을 페이지 단위로 불러오는 기능
     * @param pageable (페이지 정보)
     * @return 페이징 처리가 된 BoardEntity
     */
    Page<BoardEntity> findAll(Pageable pageable);
}
