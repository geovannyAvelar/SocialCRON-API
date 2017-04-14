package br.com.agenciacodeplus.socialcron.posts;

import br.com.agenciacodeplus.socialcron.drafts.Draft;
import br.com.agenciacodeplus.socialcron.events.Event;
import br.com.agenciacodeplus.socialcron.utils.DateUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsService {
  
  @Autowired
  private PostsDao dao;
  
  public void save(Post post) {
    dao.save(post);
  }
  
  public void save(List<Post> posts) {
    dao.save(posts);
  }
  
  public Post findOne(Long id) {
    return dao.findOne(id);
  }
  
  public List<Post> findByRange(Date from, Date to) {
    return dao.findByDateBetween(from, to);
  }
  
  public List<Post> findByDay(Date day) {
    Date limitDate = DateUtils.sumDate(day, Calendar.DAY_OF_MONTH, 1);
    return dao.findByDateBetween(day, limitDate);
  }
  
  public List<Post> findByEvent(Event event) {
    return dao.findByEvent(event);
  }
  
  public List<Post> findByDraft(Draft draft) {
    return dao.findByDraft(draft);
  }
  
  public List<Post> findAll() {
    return dao.findAll();
  }
  
  public void delete(Post post) {
    dao.delete(post);
  }
  
}
