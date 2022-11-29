package com.da0hn.coronavac.sample;

import com.da0hn.coronavac.core.annotations.Repository;

import java.util.List;

@Repository
public class ProductRepository {

  public List<Product> getPrice(final List<Product> items) {
    for (final Product product : items) {
      final Double price = (double) Math.round(30 * Math.random()); // querying price in database...
      System.out.printf("Original price of %s is %f $.", product.getName(), product.getPrice());
      product.setPrice(price);
    }
    return items;
  }

}
