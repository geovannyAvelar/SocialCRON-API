package br.com.agenciacodeplus.socialcron.posts;

import br.com.agenciacodeplus.socialcron.events.Event;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PostsDao extends JpaRepository<Post, Long> {
  public List<Post> findByDateBetween(Date from, Date to);
  public List<Post> findByEvent(Event event);
}
