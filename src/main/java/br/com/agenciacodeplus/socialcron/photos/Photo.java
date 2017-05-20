package br.com.agenciacodeplus.socialcron.photos;

import br.com.agenciacodeplus.socialcron.exceptions.EmptyFileException;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.utils.FileUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.IOException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

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
  @JsonIgnore
  private MultipartFile file;
  
  @Column(name = "filename")
  private String filename;
  
  @Column(name = "path")
  private String path;
  
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
  
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public void saveFile() throws EmptyFileException, IOException {
    String fileExtension = FileUtils.getExtension(file);
    String filename = FileUtils.generateUniqueFilename(file.getOriginalFilename(), fileExtension);
    FileUtils.saveFile(file, "src/main/resources/static/images/" + filename);
    this.filename = filename;
    this.path = "images/" + filename;
  }
  
  public File findImage() {
    if(filename == null || filename.isEmpty()) {
      throw new IllegalStateException(
                                  "Invalid filename. You should save a file before retrieve it");
    }
    
    File file = new File("src/main/resources/static/images/" + filename);
    
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
  
}
