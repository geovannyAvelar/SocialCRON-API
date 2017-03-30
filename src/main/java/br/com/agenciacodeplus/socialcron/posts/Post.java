package br.com.agenciacodeplus.socialcron.posts;

import br.com.agenciacodeplus.socialcron.drafts.Draft;
import br.com.agenciacodeplus.socialcron.events.Event;
import br.com.agenciacodeplus.socialcron.profiles.Profile;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Entity
@Table(name = "posts")
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Post {
  
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  
  @Column(name = "date")
  @NotNull
  @Future
  private Date date;
  
  @Column(name = "completed")
  private Boolean completed;
  
  @NotNull
  @ManyToOne
  @JoinColumn(name = "draft_id")
  private Draft draft;
  
  @NotNull
  @ManyToOne
  @JoinColumn(name = "profile_id")
  private Profile profile;
  
  @NotNull
  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public Draft getDraft() {
    return draft;
  }

  public void setDraft(Draft draft) {
    this.draft = draft;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }
  
}
