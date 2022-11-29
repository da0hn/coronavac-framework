package com.da0hn.coronavac.commons.exceptions;

import java.io.Serial;

public class ConfigurationNotFoundException extends CoronavacException {

  @Serial private static final long serialVersionUID = -641904895220807739L;

  public ConfigurationNotFoundException() {
  }

  public ConfigurationNotFoundException(final String message) {
    super(message);
  }

  public ConfigurationNotFoundException(
    final String message,
    final Throwable cause
  ) {
    super(message, cause);
  }

  public ConfigurationNotFoundException(final Throwable cause) {
    super(cause);
  }

}
