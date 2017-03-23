package br.com.agenciacodeplus.socialcron.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository dao;

  public User save(User user) {
    return dao.save(user);
  }

  public User find(String username) {
    return dao.findOne(username);
  }

  public User findByEmail(String email) {
    return dao.findByEmail(email);
  }

  public void delete(User user) {
    dao.delete(user);
  }

}
