package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.acl.ACLPermissions;
import br.com.agenciacodeplus.socialcron.helpers.HttpHeadersHelper;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.posts.PostsService;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/posts")
public class PostsController {
  
  private PostsService service;
  
  @Autowired
  private ACLPermissions permissions;
  
  @Autowired
  public PostsController(PostsService service) {
    this.service = service;
  }
  
  @CrossOrigin
  @PostMapping
  public @ResponseBody ResponseEntity<Void> save(@Valid @RequestBody 
                                                              Post post,
                                                              Errors errors,
                                                              Authentication authentication,
                                                              HttpHeadersHelper httpHeadersHelper) {
    if(errors.hasErrors()) {
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    
    HttpHeaders headers = null;

    if (post.getId() != null) {
      headers = httpHeadersHelper.addLocationHeader("/v2/posts", post.getId());

      if (service.exists(post.getId())) {
        return new ResponseEntity<Void>(headers, HttpStatus.CONFLICT);
      }
    }
    
    service.save(post);
    permissions.add(authentication, post);
    headers = httpHeadersHelper.addLocationHeader("/v2/posts", post.getId());
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    
  }
  
  @CrossOrigin
  @PutMapping
  @PreAuthorize(
            "hasPermission(#post.id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'write')")
  public @ResponseBody ResponseEntity<Void> update(@Valid @RequestBody Post post,
                                                                       Errors errors) {
    if(errors.hasErrors() || post.getId() == null) {
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    
    if(!service.exists(post.getId())) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    service.save(post);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'read')")
  public @ResponseBody ResponseEntity<Post> findOne(@PathVariable Long id) {
    Post post = service.findOne(id);
    
    if(post == null) {
      return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
    }
    
    return new ResponseEntity<Post>(post, HttpStatus.OK);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @PostFilter("hasPermission(filterObject, 'read')")
  public @ResponseBody List<Post> findAll() {
    return service.findAll();
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'delete')")
  public @ResponseBody ResponseEntity<Void> delete(@PathVariable Long id) {
    Post post = service.findOne(id);
    
    if(post == null) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    service.delete(post);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    
  }
  
}
