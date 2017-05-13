package br.com.agenciacodeplus.socialcron.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cannot upload an empty file")
public class EmptyFileException extends Exception {
  private static final long serialVersionUID = -4783285286486710666L;
}
