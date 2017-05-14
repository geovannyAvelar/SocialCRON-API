package br.com.agenciacodeplus.socialcron.photos;

import br.com.agenciacodeplus.socialcron.posts.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotosRepository extends JpaRepository<Photo, Long> {
  public List<Photo> findByPost(Post post);
}
