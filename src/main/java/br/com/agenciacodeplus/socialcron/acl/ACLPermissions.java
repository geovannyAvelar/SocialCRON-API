package br.com.agenciacodeplus.socialcron.acl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Service
public class ACLPermissions {

  @Autowired
  private MutableAclService mutableAclService;

  @Transactional
  public void add(Authentication authentication, Object object, Permission... permissions) {
    ObjectIdentity identity = new ObjectIdentityImpl(object);
    MutableAcl acl = mutableAclService.createAcl(identity);
    PrincipalSid principalSid = new PrincipalSid(authentication);
    
    for (Permission permission : permissions) {
      acl.insertAce(acl.getEntries().size(), permission, principalSid, true);
    }
    
    mutableAclService.updateAcl(acl);
  }
  
  @Transactional
  public void add(Authentication authentication, Object object) {
    add(authentication, object, BasePermission.CREATE, BasePermission.READ, 
                                BasePermission.WRITE, BasePermission.DELETE);
  }
  
  @Transactional
  public void add(Authentication authentication, List objects, Permission... permissions) {
    Iterator iterator = objects.iterator();
    while(iterator.hasNext()) {
      add(authentication, iterator.next(), permissions);
    }
  }
  
  @Transactional
  public void add(Authentication authentication, List objects) {
    add(authentication, objects, BasePermission.CREATE, BasePermission.READ, 
                                 BasePermission.WRITE, BasePermission.DELETE);
  }
  
}
