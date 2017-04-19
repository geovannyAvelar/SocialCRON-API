package br.com.agenciacodeplus.socialcron.schedules;

import br.com.agenciacodeplus.socialcron.events.Event;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.utils.DateUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulesService {
  
  @Autowired
  private SchedulesDao dao;
  
  public void save(Schedule post) {
    dao.save(post);
  }
  
  public void save(List<Schedule> schedules) {
    dao.save(schedules);
  }
  
  public Schedule findOne(Long id) {
    return dao.findOne(id);
  }
  
  public List<Schedule> findByRange(Date from, Date to) {
    return dao.findByDateBetween(from, to);
  }
  
  public List<Schedule> findByDay(Date day) {
    Date limitDate = DateUtils.sumDate(day, Calendar.DAY_OF_MONTH, 1);
    return dao.findByDateBetween(day, limitDate);
  }
  
  public List<Schedule> findByEvent(Event event) {
    return dao.findByEvent(event);
  }
  
  public List<Schedule> findByPost(Post post) {
    return dao.findByPost(post);
  }
  
  public List<Schedule> findAll() {
    return dao.findAll();
  }
  
  public void delete(Schedule schedule) {
    dao.delete(schedule);
  }
  
}
