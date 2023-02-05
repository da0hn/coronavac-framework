package com.da0hn.coronavac.sample;

import com.da0hn.coronavac.core.annotations.Component;

import java.util.List;

@Component
public class ProductService {

  private ProductRepository repository;

  public List<Product> getFinalPrice(final List<Product> items) {
    final var list = this.repository.getPrice(items);
    for (final var product : list) {
      product.setPrice(product.getPrice() * (100 - product.getDiscount()) / 100);
      System.out.printf(
        "Price of %s after %d discount is %f%n",
        product.getName(),
        product.getDiscount(),
        product.getPrice()
      );
    }
    return list;
  }

}
