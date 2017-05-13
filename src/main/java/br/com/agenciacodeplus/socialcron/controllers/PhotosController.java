package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.acl.ACLPermissions;
import br.com.agenciacodeplus.socialcron.exceptions.EmptyFileException;
import br.com.agenciacodeplus.socialcron.photos.Photo;
import br.com.agenciacodeplus.socialcron.photos.PhotosService;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.posts.PostsService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v2/photos")
public class PhotosController {
  
  private PhotosService service;
  
  private PostsService postsService;
  
  @Autowired
  private ACLPermissions permissions;
  
  @Autowired
  public PhotosController(PhotosService service, PostsService postsService) {
    this.service = service;
    this.postsService = postsService;
  }
  
  @CrossOrigin
  @PostMapping
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'write')")
  public @ResponseBody ResponseEntity<Void> save(
                            @RequestParam("postId") Long id,
                            @RequestParam("file") MultipartFile file,
                                                 Authentication authentication) 
                                                                        throws EmptyFileException,
                                                                               IOException,
                                                                               URISyntaxException {
    Post post = postsService.findOne(id);
    
    if(post != null) {
      Photo photo = new Photo();
      photo.setPost(post);
      photo.setFile(file);
      photo.saveFile();
      service.save(photo);
      permissions.add(authentication, photo);
      
      post.addPhoto(photo);
      postsService.save(post);
      
      return ResponseEntity.created(new URI("/v2/photos/" + post.getId())).build();
    }
    
    return ResponseEntity.badRequest().build();
    
  }
  
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize(
            "hasPermission(#id, 'br.com.agenciacodeplus.socialcron.photos.Photo', 'read')")
  public ResponseEntity<InputStreamResource> findOne(@PathVariable Long id) 
                                                                      throws FileNotFoundException {
    Photo photo = service.findOne(id);
    
    if(photo == null) {
      return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
    }
    
    File image = photo.findImage();
    InputStream fileStream = new FileInputStream(image);
    
    return ResponseEntity
                .ok()
                .contentLength(image.length())
                .contentType(MediaType.parseMediaType(photo.getMediaType()))
                .body(new InputStreamResource(fileStream));
  }
  
}
