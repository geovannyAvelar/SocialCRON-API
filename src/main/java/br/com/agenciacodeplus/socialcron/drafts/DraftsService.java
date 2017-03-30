package br.com.agenciacodeplus.socialcron.drafts;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DraftsService {
  
  @Autowired
  private DraftsDao dao;
 
  public void save(Draft draft) {
    dao.save(draft);
  }
  
  public Draft findOne(Long id) {
    return dao.findOne(id);
  }
  
  public List<Draft> findAll() {
    return dao.findAll();
  }
  
  public boolean exists(Long id) {
    return dao.exists(id);
  }
  
  public void delete(Draft draft) {
    dao.delete(draft);
  }
  
}
