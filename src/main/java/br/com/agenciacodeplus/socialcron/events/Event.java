package br.com.agenciacodeplus.socialcron.events;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.agenciacodeplus.socialcron.periods.Period;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.profiles.Profile;
import br.com.agenciacodeplus.socialcron.schedules.Schedule;
import br.com.agenciacodeplus.socialcron.utils.DateUtils;

@Entity
@Table(name = "events")
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Event {
  
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  
  @Column(name = "initial_date")
  @NotNull
  @Future
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private Date initialDate;
  
  @Column(name = "limit_date")
  @Future
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date limitDate;
  
  @Column(name = "period")
  private Period period;
  
  @Column(name = "time_interval")
  @Min(1)
  private Integer interval;
    
  @NotNull
  @Transient
  @JsonInclude(JsonInclude.Include.NON_NULL) 
  private Post draft;
  
  @NotNull
  @Transient
  @JsonInclude(JsonInclude.Include.NON_NULL) 
  private Profile profile;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(Date initialDate) {
    this.initialDate = initialDate;
  }

  public Date getLimitDate() {
    return limitDate;
  }

  public void setLimitDate(Date limitDate) {
    this.limitDate = limitDate;
  }

  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }
  
  public Post getDraft() {
    return draft;
  }

  public void setDraft(Post draft) {
    this.draft = draft;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }
  
  /**
   * Based on a initial date, a limit date and a repetition period (if exists),  create posts 
   * in this interval
   * @return List<Post> A list with all posts in event period
   */
  public List<Schedule> generatePosts() {
    List<Schedule> posts = new LinkedList<Schedule>();
    Date nextDate = initialDate;
 
    if (limitDate != null) {
      /*
       * Repetition period (minute, hour, day...) represented by constants of Calendar class
       */
      Integer period = DateUtils.stringToDateConstant(this.period.toString());
      
      /*
       * Get all the dates between initial date and limit date and creates the list with posts in
       * this interval
       */
      while (true) {
        nextDate = DateUtils.sumDate(nextDate, period, interval);
        posts.add(createPost(nextDate));
 
        /*
         * Sum one unity of time, thus the last date will be included too
         */
        if (nextDate.after(DateUtils.sumDate(limitDate, period, 1))) {
          break;
        }
      }
    } else {
      // There is no limit date, the post happens only one time
      posts.add(createPost(initialDate));
    }
    
    return posts;
    
  }
 
  public boolean equals(Object object) {
    if(!object.getClass().equals(Event.class)) return false;
    
    Event event = (Event) object;
    
    if(event.getId() == null) return false;
    
    return event.getId().equals(this.id);
  }
  
  private Schedule createPost(Date date) {
    Schedule post = new Schedule();
    post.setDate(date);
    post.setPost(draft);
    post.setProfile(profile);
    post.setEvent(this);
    post.setCompleted(false);
    return post;
  }
}
