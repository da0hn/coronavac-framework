package com.da0hn.coronavac.commons.exceptions;

import java.io.Serial;

public abstract class CoronavacException extends RuntimeException {

  @Serial private static final long serialVersionUID = -4294529138339873205L;

  protected CoronavacException() {
  }

  protected CoronavacException(final String message) {
    super(message);
  }

  protected CoronavacException(
    final String message,
    final Object... arguments
  ) {
    super(String.format(message, arguments));
  }
  
  protected CoronavacException(
    Throwable cause,
    final String message,
    final Object... arguments
  ) {
    super(String.format(message, arguments), cause);
  }

  protected CoronavacException(
    final String message,
    final Throwable cause
  ) {
    super(message, cause);
  }

  protected CoronavacException(final Throwable cause) {
    super(cause);
  }

}
