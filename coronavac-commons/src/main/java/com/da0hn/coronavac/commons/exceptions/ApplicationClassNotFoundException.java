package com.da0hn.coronavac.commons.exceptions;

import java.io.Serial;

public class ApplicationClassNotFoundException extends CoronavacException {

  @Serial private static final long serialVersionUID = -641904895220807739L;

  public ApplicationClassNotFoundException() {
  }

  public ApplicationClassNotFoundException(final String message) {
    super(message);
  }

  public ApplicationClassNotFoundException(
    final String message,
    final Throwable cause
  ) {
    super(message, cause);
  }

  public ApplicationClassNotFoundException(final Throwable cause) {
    super(cause);
  }

}
