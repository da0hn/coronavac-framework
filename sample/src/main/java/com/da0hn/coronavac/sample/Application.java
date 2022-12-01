package com.da0hn.coronavac.sample;

import com.da0hn.coronavac.core.ApplicationContext;
import com.da0hn.coronavac.core.annotations.CoronavacApplication;


@CoronavacApplication
public final class Application {

  public static void main(final String[] args) {

    final ApplicationContext context = new ApplicationContext(Application.class);
    final var productService = context.find(ProductService.class);
  }

}
