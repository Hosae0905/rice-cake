package project.ricecake.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ricecake.board.domain.entity.BoardEntity;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Optional<BoardEntity> findByBoardIdx(Long boardIdx);
}
