package br.com.agenciacodeplus.socialcron.posts;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PostsService {
  
  @Autowired
  private PostsDao dao;
 
  public void save(Post post) {
    dao.save(post);
  }
  
  public Post findOne(Long id) {
    return dao.findOne(id);
  }
  
  public List<Post> findAll() {
    return dao.findAll();
  }
  
  public boolean exists(Long id) {
    return dao.exists(id);
  }
  
  public void delete(Post post) {
    dao.delete(post);
  }
  
}
