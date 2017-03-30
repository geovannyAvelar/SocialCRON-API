package br.com.agenciacodeplus.socialcron.events;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsService {
  
  @Autowired
  private EventsDao dao;
  
  public void save(Event event) {
    dao.save(event);
  }
  
  public Event findOne(Long id) {
    return dao.findOne(id);
  }
  
  public List<Event> findAll() {
    return dao.findAll();
  }
  
  public boolean exists(Long id) {
    return dao.exists(id);
  }
  
  public void delete(Event event) {
    dao.delete(event);
  }
  
}
