package br.com.agenciacodeplus.socialcron.profiles;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Entity
@Table(name = "profiles")
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Profile {
  
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  
  @Column(name = "profile_id")
  @NotNull
  private String profileId;
  
  @Column(name = "name")
  @NotNull
  @NotEmpty
  @Size(max = 256)
  private String name;
  
  @Column(name = "token")
  @NotNull
  @NotEmpty
  private String token;
  
  @Column(name = "created_at")
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mmZ")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mmZ")
  private Date createdAt;
  
  @Column(name = "expires")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mmZ")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mmZ")
  private Date expires;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProfileId() {
    return profileId;
  }

  public void setProfileId(String profileId) {
    this.profileId = profileId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(Date expires) {
    this.expires = expires;
  }
  
}
