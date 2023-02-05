package com.da0hn.coronavac.sample;

import com.da0hn.coronavac.core.annotations.Component;
import com.da0hn.coronavac.core.annotations.Get;

import java.util.List;

@Component
public class GetProductService {

  private ProductService service;


  @Get
  public GetProductService(final ProductService service) {
    this.service = service;
  }

  public void setService(final ProductService service) {
    this.service = service;
  }

  public List<Product> get() {
    return this.service.getFinalPrice(List.of(
      new Product("p1", 12),
      new Product("p2", 23),
      new Product("p3", 28),
      new Product("p4", 100),
      new Product("p5", 10),
      new Product("p6", 15)
    ));
  }

}
