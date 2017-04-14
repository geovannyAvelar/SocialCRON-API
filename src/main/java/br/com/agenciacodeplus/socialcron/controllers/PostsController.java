package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.drafts.Draft;
import br.com.agenciacodeplus.socialcron.drafts.DraftsService;
import br.com.agenciacodeplus.socialcron.events.Event;
import br.com.agenciacodeplus.socialcron.events.EventsService;
import br.com.agenciacodeplus.socialcron.exceptions.ResourceNotFoundException;
import br.com.agenciacodeplus.socialcron.periods.Period;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.posts.PostsService;
import br.com.agenciacodeplus.socialcron.profiles.Profile;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/posts")
public class PostsController {

  private PostsService service;
  
  private EventsService eventsService;
  
  private DraftsService draftsService;
  
  @Autowired
  public PostsController(PostsService service, 
                         EventsService eventsService, 
                         DraftsService draftsService) {
    this.service = service;
    this.eventsService = eventsService;
    this.draftsService = draftsService;
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'read')")
  public @ResponseBody ResponseEntity<Post> findOne(@PathVariable Long id) {
    Post post = service.findOne(id);
    
    if(post == null) {
      new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
    }
    
    return new ResponseEntity<Post>(post, HttpStatus.OK);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/day/{day}", method = RequestMethod.GET)
  @PostFilter("hasAuthority('ADMIN') or hasPermission(filterObject, 'read')")
  public @ResponseBody List<Post> findByPeriod(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd")
                                                Date day) {
    return service.findByDay(day);
  }
  
  @CrossOrigin
  @RequestMapping(value = "/day/2014-11-18", method = RequestMethod.GET)
  public @ResponseBody Post findByPeriod() {
    Draft draft = new Draft();
    draft.setId(0l);
    draft.setTitle("Never send to know...");
    draft.setContent("No man is an island, entire of itself; every man is a piece of the continent,"
        + " a part of the main. If a clod be washed away by the sea, Europe is the less, as well as"
        + " if a promontory were, as well as if a manor of thy friend's or of thine own were: any "
        + "man's death diminishes me, because I am involved in mankind, and therefore never send to"
        + " know for whom the bells tolls; it tolls for thee. (Devotions Upon Emergent Occasions, "
        + "'Meditation XVII', John Donne). "
        + "\n In memoriam Sérgio Carneiro (24-12-1953 ~ 18-11-2014)");
   
    Event event = new Event();
    event.setId(0l);
    
    Calendar calendar = new GregorianCalendar();
    
    calendar.set(1953, 11, 24, 0, 0, 0);
    event.setInitialDate(calendar.getTime());
    
    calendar.set(2014, 10, 18, 0, 0, 0);
    event.setLimitDate(calendar.getTime());
    
    event.setPeriod(Period.FOREVER);
    event.setDraft(draft);
    
    Profile profile = new Profile();
    profile.setId(0l);
    profile.setName("Sérgio Carneiro");
    profile.setToken("YW5kIHRoZSBkdXN0IHJldHVybnMgdG8gdGhlIGdyb3VuZCBpdCBjYW1lIGZyb20sIGFuZCB0aGUg"
                    + "c3Bpcml0IHJldHVybnMgdG8gR29kIHdobyBnYXZlIGl0Lg==");
    
    Post post = new Post();
    post.setDate(calendar.getTime());
    post.setId(0l);
    post.setCompleted(true);
    post.setDraft(draft);
    post.setEvent(event);
    post.setProfile(profile);
    
    return post;
  }
  
  @CrossOrigin
  @RequestMapping(value = "/draft/{id}", method = RequestMethod.GET)
  @PostFilter("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.drafts.Draft', 'read')"
            + " and hasPermission(filterObject, 'read')")
  public @ResponseBody List<Post> findByDraft(@PathVariable Long id) 
                                                           throws ResourceNotFoundException {
    Draft draft = draftsService.findOne(id);
    
    if(draft == null) {
      throw new ResourceNotFoundException();
    }
    
    return service.findByDraft(draft);
  }
  
  @CrossOrigin
  @RequestMapping(value = "/event/{id}", method = RequestMethod.GET)
  @PostFilter("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.events.Event', 'read')"
            + " and hasPermission(filterObject, 'read')")
  public @ResponseBody List<Post> findByEvent(@PathVariable Long id) 
                                                           throws ResourceNotFoundException {
    Event event = eventsService.findOne(id);
    
    if(event == null) {
      throw new ResourceNotFoundException();
    }
    
    return service.findByEvent(event);
  }
  
  @CrossOrigin
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @PostFilter("hasAuthority('ADMIN') or hasPermission(filterObject, 'read')")
  public @ResponseBody List<Post> findAll() {   
    return service.findAll(); 
  }
  
}
