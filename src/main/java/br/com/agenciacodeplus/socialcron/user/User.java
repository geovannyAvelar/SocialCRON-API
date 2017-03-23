package br.com.agenciacodeplus.socialcron.user;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import br.com.agenciacodeplus.socialcron.authority.Authority;

@Entity
@Component
@Table(name = "USER")
public class User {

  @Id
  @Column(name = "USERNAME")
  @Size(min = 6, max = 16)
  private String username;
  
  @Column(name = "EMAIL", unique = true)
  @Email
  private String email;

  @Column(name = "PASSWORD")
  @Size(min = 0, max = 500)
  private String password;

  @Column(name = "NAME")
  @NotBlank
  @Size(max = 50)
  private String name;

  @Column(name = "ACTIVATED")
  private boolean activated;

  @Column(name = "ACTIVATIONKEY")
  @Size(min = 0, max = 100)
  private String activationKey;

  @Column(name = "RESETPASSWORDKEY")
  @Size(min = 0, max = 100)
  private String resetPasswordKey;

  @ManyToMany
  @JoinTable(name = "USER_AUTHORITY", joinColumns = @JoinColumn(name = "USERNAME"),
      inverseJoinColumns = @JoinColumn(name = "AUTHORITY"))
  private Set<Authority> authorities;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public String getResetPasswordKey() {
    return resetPasswordKey;
  }

  public void setResetPasswordKey(String resetPasswordKey) {
    this.resetPasswordKey = resetPasswordKey;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    User user = (User) o;

    if (!email.equals(user.email))
      return false;

    return true;
  }

  @Override
  public String toString() {
    return "User{" + " password='" + password + '\'' + ", email='"
        + email + '\'' + ", activated='" + activated + '\'' + ", activationKey='" + activationKey
        + '\'' + ", resetPasswordKey='" + resetPasswordKey + '\'' + ", authorities=" + authorities
        + '}';
  }

}
