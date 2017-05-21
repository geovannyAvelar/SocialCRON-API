package br.com.agenciacodeplus.socialcron.schedules;

import br.com.agenciacodeplus.socialcron.events.Event;
import br.com.agenciacodeplus.socialcron.posts.Post;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulesDao extends JpaRepository<Schedule, Long> {
  public List<Schedule> findByDateBetween(Date from, Date to);
  public List<Schedule> findByEvent(Event event);
  public List<Schedule> findByPost(Post post);
}
