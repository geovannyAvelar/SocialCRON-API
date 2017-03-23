// Credits to Rajith Delantha https://github.com/rajithd
package br.com.agenciacodeplus.socialcron.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  public void commence(HttpServletRequest request, 
                       HttpServletResponse response, AuthenticationException ae) 
                                                             throws IOException, ServletException {

    log.info("Pre-authenticated entry point called. Rejecting access");
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");

  }
}
