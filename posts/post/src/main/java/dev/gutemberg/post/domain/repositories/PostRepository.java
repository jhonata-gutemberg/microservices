package dev.gutemberg.post.domain.repositories;

import dev.gutemberg.post.domain.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
