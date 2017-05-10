package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.acl.ACLPermissions;
import br.com.agenciacodeplus.socialcron.facebook.FacebookTokenHandler;
import br.com.agenciacodeplus.socialcron.helpers.HttpHeadersHelper;
import br.com.agenciacodeplus.socialcron.profiles.Profile;
import br.com.agenciacodeplus.socialcron.profiles.ProfilesService;
import br.com.agenciacodeplus.socialcron.utils.DateUtils;
import facebook4j.auth.AccessToken;

import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/profiles")
public class ProfilesController {
  
  private ProfilesService service;
  
  @Autowired
  private ACLPermissions permissions;
  
  @Autowired
  public ProfilesController(ProfilesService service) {
    this.service = service;
  }
  
  @CrossOrigin
  @PostMapping
  public @ResponseBody ResponseEntity<Void> save(@Valid @RequestBody 
                                                          Profile profile, 
                                                          Errors errors,
                                                          Authentication authentication,
                                                          HttpHeadersHelper httpHeadersHelper,
                                                          FacebookTokenHandler fbTokenHandler) {
    
    if(errors.hasErrors()) {    
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    
    HttpHeaders headers = null;

    if (profile.getId() != null) {
      headers = httpHeadersHelper.addLocationHeader("/v1/profiles", profile.getId());

      if (service.exists(profile.getId())) {
        return new ResponseEntity<Void>(headers, HttpStatus.CONFLICT);
      }
    }
    
    AccessToken longLivedToken = fbTokenHandler.refreshToken(new AccessToken(profile.getToken()));
    
    if(longLivedToken == null) {
      return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    profile.setToken(longLivedToken.getToken());
    // Facebook long lived tokens expires in two months
    profile.setExpires(DateUtils.sumDate(profile.getCreatedAt(), Calendar.MONTH, 2));
    service.save(profile);
    permissions.add(authentication, profile);
    headers = httpHeadersHelper.addLocationHeader("/v1/profiles", profile.getId());
    return new ResponseEntity<Void>(HttpStatus.CREATED);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize(
      "hasPermission(#id, 'br.com.agenciacodeplus.socialcron.profiles.Profile', 'read')")
  public @ResponseBody ResponseEntity<Profile> find(@PathVariable Long id) {
    Profile profile = service.findOne(id);
    
    if(profile == null) {
      return new ResponseEntity<Profile>(HttpStatus.NOT_FOUND);
    }
    
    return new ResponseEntity<Profile>(profile, HttpStatus.OK);
  }
  
  @CrossOrigin
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @PostFilter("hasPermission(filterObject, 'read')")
  public @ResponseBody List<Profile> findAll() {
    return service.findAll();
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @PreAuthorize(
            "hasPermission(#id, 'br.com.agenciacodeplus.socialcron.profiles.Profile', 'delete')")
  public @ResponseBody ResponseEntity<Void> delete(@PathVariable Long id) {
    Profile profile = service.findOne(id);
    
    if(profile == null) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    service.delete(profile);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    
  }
  
}
