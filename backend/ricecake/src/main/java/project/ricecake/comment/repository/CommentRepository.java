package project.ricecake.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.comment.domain.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByBoard(Pageable pageable, BoardEntity board);
}
