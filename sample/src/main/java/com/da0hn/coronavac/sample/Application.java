package com.da0hn.coronavac.sample;

import com.da0hn.coronavac.core.ApplicationContext;
import com.da0hn.coronavac.core.annotations.CoronavacApplication;

import java.util.List;


@CoronavacApplication
public final class Application {

  public static void main(final String[] args) {

    final ApplicationContext context = new ApplicationContext(Application.class);
    
    // Test if instance was found with dependencies injected
    final var productService = context.find(ProductService.class);
    productService.getFinalPrice(List.of(
      new Product("p1", 30),
      new Product("p2", 25),
      new Product("p3", 75)
    ));
  }

}
