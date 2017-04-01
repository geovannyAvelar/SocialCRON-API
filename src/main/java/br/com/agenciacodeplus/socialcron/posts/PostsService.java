package br.com.agenciacodeplus.socialcron.posts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agenciacodeplus.socialcron.utils.DateUtils;

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
  
  public List<Post> findAll() {
    return dao.findAll();
  }
  
  public void delete(Post post) {
    dao.delete(post);
  }
  
}
