package br.com.agenciacodeplus.socialcron.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class ResourceNotFoundException extends Exception {
  private static final long serialVersionUID = 7145448309015223539L;
}
