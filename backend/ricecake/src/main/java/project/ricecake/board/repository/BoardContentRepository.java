package project.ricecake.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ricecake.board.domain.entity.BoardContent;

@Repository
public interface BoardContentRepository extends JpaRepository<BoardContent, Long> {
}
