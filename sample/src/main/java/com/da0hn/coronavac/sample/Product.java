package com.da0hn.coronavac.sample;

public class Product {

  private String name;
  private Integer discount;
  private Double price;

  public Product(
    final String name,
    final Integer discount
  ) {
    this.name = name;
    this.discount = discount;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Integer getDiscount() {
    return this.discount;
  }

  public void setDiscount(final Integer discount) {
    this.discount = discount;
  }

  public Double getPrice() {
    return this.price;
  }

  public void setPrice(final Double price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Product{" +
           "name='" + this.name + '\'' +
           ", discount=" + this.discount +
           ", price=" + this.price +
           '}';
  }

}
