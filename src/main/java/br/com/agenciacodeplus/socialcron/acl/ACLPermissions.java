package br.com.agenciacodeplus.socialcron.acl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  
}
