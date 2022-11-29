package com.da0hn.coronavac.commons.exceptions;

import java.io.Serial;

public class PackageNotFoundException extends CoronavacException {

  @Serial private static final long serialVersionUID = -5883220285328305318L;

  public PackageNotFoundException() {
  }

  public PackageNotFoundException(
    final String message,
    final Object... arguments
  ) {
    super(message, arguments);
  }

}
