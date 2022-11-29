package com.da0hn.coronavac.commons.exceptions;

import java.io.Serial;

public abstract class CoronavacException extends RuntimeException {

  @Serial private static final long serialVersionUID = -4294529138339873205L;

  public CoronavacException() {
  }

  public CoronavacException(final String message) {
    super(message);
  }

  public CoronavacException(
    final String message,
    final Throwable cause
  ) {
    super(message, cause);
  }

  public CoronavacException(final Throwable cause) {
    super(cause);
  }

}
