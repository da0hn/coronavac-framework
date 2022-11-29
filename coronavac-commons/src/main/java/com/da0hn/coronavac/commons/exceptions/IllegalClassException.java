package com.da0hn.coronavac.commons.exceptions;

import java.io.Serial;

public class IllegalClassException extends CoronavacException {

  @Serial private static final long serialVersionUID = -5883220285328305318L;

  public IllegalClassException() {
  }

  public IllegalClassException(
    final String message,
    final Object... arguments
  ) {
    super(message, arguments);
  }

  public IllegalClassException(
    final Throwable cause,
    final String message,
    final Object... arguments
  ) {
    super(cause, message, arguments);
  }

  public IllegalClassException(final Throwable cause) {
    super(cause);
  }

}
