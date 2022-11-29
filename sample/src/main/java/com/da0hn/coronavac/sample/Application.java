package com.da0hn.coronavac.sample;

import com.da0hn.coronavac.core.ApplicationContext;

public final class Application {

  public static void main(final String[] args) {

    final ApplicationContext context = new ApplicationContext(ApplicationConfig.class);
    final var productService = context.find(ProductService.class);
  }

}
