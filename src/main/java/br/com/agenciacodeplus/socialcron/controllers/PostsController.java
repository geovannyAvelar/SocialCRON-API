package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.posts.PostsService;
import java.util.Date;
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
  
  @Autowired
  public PostsController(PostsService service) {
    this.service = service;
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
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @PostFilter("hasAuthority('ADMIN') or hasPermission(filterObject, 'read')")
  public @ResponseBody List<Post> findAll() {   
    return service.findAll(); 
  }
  
}
