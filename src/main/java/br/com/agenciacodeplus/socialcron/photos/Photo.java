package br.com.agenciacodeplus.socialcron.photos;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.agenciacodeplus.socialcron.exceptions.EmptyFileException;
import br.com.agenciacodeplus.socialcron.posts.Post;

@Entity
@Table(name = "photos")
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Photo {
  
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  
  @Transient
  private MultipartFile file;
  
  @Column(name = "filename")
  @JsonIgnore
  private String filename;
  
  @Column(name = "media_type")
  private String mediaType;
  
  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "post_id", referencedColumnName = "ID")
  private Post post;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public void saveFile() throws EmptyFileException, IOException {
    String fileExtension = getExtension(file.getOriginalFilename());
    String filename = generateUniqueFilename(file.getOriginalFilename(), fileExtension);
    this.mediaType = "application/" + fileExtension.replace(".", "");
    
    // FIXME File path shouldn't be hardcoded
    if (!file.isEmpty()) {
      byte[] bytes = file.getBytes();
      File file = new File("/var/socialcron/images/" + filename);
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
      stream.write(bytes);
      stream.close();
      this.filename = filename;
    } else {
      throw new EmptyFileException();
    }
  }
  
  public File findImage() {
    if(filename == null || filename.isEmpty()) {
      throw new IllegalStateException(
                                  "Invalid filename. You should save a file before retrieve it");
    }
    
    File file = new File("/var/socialcron/images/" + filename);
    
    if(!file.exists()) {
      throw new IllegalStateException("File cannot be found");
    }
    
    return file;
    
  }
  
  public boolean equals(Object object) {
    if(!object.getClass().equals(Photo.class)) {
      return false;
    }
    
    Photo photo = (Photo) object;
    
    if(photo.getId() == null) {
      return false;
    }
    
    return photo.getId().equals(this.id);
    
  }
  
  // FIXME Out of scope. This method should be in an auxilary class
  private String generateUniqueFilename(String seed, String extension) {
    String dataToHash = new Long(new Date().getTime()).toString() + seed;
    String filename = Base64.encodeBase64URLSafeString(dataToHash.getBytes()) + extension;
    return filename;
  }
  
  //FIXME Out of scope. This method should be in an auxilary class
  private String getExtension(String filename) {
    int length = filename.length();
    
    if(length < 5) {
      throw new IllegalArgumentException("Invalid filename. A file name should be greater than 5");
    }
    
    return filename.substring(length - 4, length);
    
  }
  
}
