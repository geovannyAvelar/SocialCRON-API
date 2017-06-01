package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.acl.ACLPermissions;
import br.com.agenciacodeplus.socialcron.dispatcher.Dispatcher;
import br.com.agenciacodeplus.socialcron.events.Event;
import br.com.agenciacodeplus.socialcron.events.EventsService;
import br.com.agenciacodeplus.socialcron.events.EventsValidator;
import br.com.agenciacodeplus.socialcron.helpers.HttpHeadersHelper;
import br.com.agenciacodeplus.socialcron.schedules.Schedule;
import br.com.agenciacodeplus.socialcron.schedules.SchedulesService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/events")
public class EventsController {
  
  private EventsService service;
  
  private SchedulesService schedulesService;
  
  @Autowired
  private EventsValidator eventsValidator;
  
  @Autowired
  private ACLPermissions permissions;
  
  @Autowired
  public EventsController(EventsService service, SchedulesService postService) {
    this.service = service;
    this.schedulesService = postService;
  }
  
  @InitBinder("event")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(eventsValidator);
  }
  
  @CrossOrigin
  @PostMapping
  public @ResponseBody ResponseEntity<Void> save(@Valid @RequestBody 
                                                              Event event, 
                                                              Errors errors,
                                                              Authentication authentication,
                                                              HttpHeadersHelper httpHeadersHelper,
                                                              Dispatcher dispatcher) {
    
    if(errors.hasErrors()) {
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    
    HttpHeaders headers = null;

    if (event.getId() != null) {
      headers = httpHeadersHelper.addLocationHeader("/v1/events", event.getId());

      if (service.exists(event.getId())) {
        return new ResponseEntity<Void>(headers, HttpStatus.CONFLICT);
      }
    }
    
    service.save(event);
    permissions.add(authentication, event);
    headers = httpHeadersHelper.addLocationHeader("/v1/events", event.getId());
    
    List<Schedule> schedules = event.generatePosts();
    schedulesService.save(schedules);
    permissions.add(authentication, schedules);
    
    
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize(
      "hasPermission(#id, 'br.com.agenciacodeplus.socialcron.events.Event', 'read')")
  public @ResponseBody ResponseEntity<Event> find(@PathVariable Long id) {
    Event event = service.findOne(id);
    
    if(event == null) {
      return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
    }
    
    return new ResponseEntity<Event>(event, HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @PostFilter("hasPermission(filterObject, 'read')")
  public @ResponseBody List<Event> findAll() {
    return service.findAll();
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.events.Event', 'delete')")
  public @ResponseBody ResponseEntity<Void> delete(@PathVariable Long id) {
    Event event = service.findOne(id);
    
    if(event == null) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    service.delete(event);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    
  }
  
}
