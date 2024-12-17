package project.ricecake.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ricecake.comment.domain.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
