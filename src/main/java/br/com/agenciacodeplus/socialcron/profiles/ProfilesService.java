package br.com.agenciacodeplus.socialcron.profiles;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProfilesService {
  
  @Autowired
  private ProfilesDao dao;
  
  public void save(Profile profile) {
    dao.save(profile);
  }
  
  public Profile findOne(Long id) {
    return dao.findOne(id);
  }
  
  public List<Profile> findAll() {
    return dao.findAll();
  }
  
  public boolean exists(Long id) {
    return dao.exists(id);
  }
  
  public void delete(Profile profile) {
    dao.delete(profile);
  }
  
}
