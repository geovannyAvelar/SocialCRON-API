package br.com.agenciacodeplus.socialcron.photos;

import br.com.agenciacodeplus.socialcron.exceptions.EmptyFileException;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.utils.FileUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
  @JsonIgnore
  private String filename;
  
  @Transient
  private String base64Image;
  
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

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }
  
  public String getBase64Image() {
    return base64Image;
  }

  public void setBase64Image(String base64Image) {
    this.base64Image = base64Image;
  }

  public void saveFile() throws EmptyFileException, IOException {
    String fileExtension = FileUtils.getExtension(file);
    String filename = FileUtils.generateUniqueFilename(file.getOriginalFilename(), fileExtension);
    FileUtils.saveFile(file, "/var/socialcron/images/" + filename);
    this.filename = filename;
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
  
  public String generateImageBase64() throws IOException {
    File image = findImage();
    byte[] imageBytes = Files.readAllBytes(image.toPath());
    String base64Image = new String(Base64.encodeBase64(imageBytes), "UTF-8");
    this.base64Image = base64Image;
    return base64Image;
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
