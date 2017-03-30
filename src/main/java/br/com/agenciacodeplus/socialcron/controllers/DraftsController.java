package br.com.agenciacodeplus.socialcron.controllers;

import javax.validation.Valid;
import br.com.agenciacodeplus.socialcron.acl.ACLPermissions;
import br.com.agenciacodeplus.socialcron.drafts.Draft;
import br.com.agenciacodeplus.socialcron.drafts.DraftsService;
import br.com.agenciacodeplus.socialcron.helpers.HttpHeadersHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/v1/drafts")
public class DraftsController {
  
  private DraftsService service;
  
  @Autowired
  private ACLPermissions permissions;
  
  @Autowired
  public DraftsController(DraftsService service) {
    this.service = service;
  }
  
  @CrossOrigin
  @PostMapping
  public @ResponseBody ResponseEntity<Void> save(@Valid @RequestBody 
                                                              Draft draft,
                                                              Errors errors,
                                                              Authentication authentication,
                                                              HttpHeadersHelper httpHeadersHelper) {
    if(errors.hasErrors()) {
      return new ResponseEntity<Void>(HttpStatus.BAD_GATEWAY);
    }
    
    HttpHeaders headers = null;

    if (draft.getId() != null) {
      headers = httpHeadersHelper.addLocationHeader("/v1/drafts", draft.getId());

      if (service.exists(draft.getId())) {
        return new ResponseEntity<Void>(headers, HttpStatus.CONFLICT);
      }
    }
    
    service.save(draft);
    permissions.add(authentication, draft);
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    
  }
  
  @CrossOrigin
  @PutMapping
  @PreAuthorize(
            "hasPermission(#draft.id, 'br.com.agenciacodeplus.socialcron.drafts.Draft', 'write')")
  public @ResponseBody ResponseEntity<Void> update(@Valid @RequestBody Draft draft,
                                                                       Errors errors) {
    if(errors.hasErrors() || draft.getId() == null) {
      return new ResponseEntity<Void>(HttpStatus.BAD_GATEWAY);
    }
    
    if(!service.exists(draft.getId())) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    service.save(draft);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.drafts.Draft', 'read')")
  public @ResponseBody ResponseEntity<Draft> findOne(@PathVariable Long id) {
    Draft draft = service.findOne(id);
    
    if(draft == null) {
      return new ResponseEntity<Draft>(HttpStatus.NOT_FOUND);
    }
    
    return new ResponseEntity<Draft>(draft, HttpStatus.OK);
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.drafts.Draft', 'delete')")
  public @ResponseBody ResponseEntity<Void> delete(@PathVariable Long id) {
    Draft draft = service.findOne(id);
    
    if(draft == null) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    
    service.delete(draft);
    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    
  }
  
}
