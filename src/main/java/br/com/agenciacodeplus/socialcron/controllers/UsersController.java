package br.com.agenciacodeplus.socialcron.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.agenciacodeplus.socialcron.user.User;
import br.com.agenciacodeplus.socialcron.user.UserService;

@RestController
@RequestMapping("/v1/users")
public class UsersController {
  
  private UserService service;
  
  @Autowired
  public UsersController(UserService service) {
    this.service = service;
  }
 
  /**
   * Return data about current user
   * @param principal java.security.Principal with user data
   * @return ResponseEntity<User> with user
   */
  @CrossOrigin
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("isAuthenticated()")
  public @ResponseBody ResponseEntity<User> getUser(Principal principal) {
    User user = null;
    String name = principal.getName();
    
    if (name.contains("@")) {
      user = service.findByEmail(name);
    } else {
      user = service.find(name);
    }
    
    return new ResponseEntity<User>(user, HttpStatus.OK);
  }
  
}
