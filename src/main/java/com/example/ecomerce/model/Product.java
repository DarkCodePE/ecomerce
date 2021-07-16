package com.example.ecomerce.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.NaturalId;


@Entity(name = "product")
@Table(name="products")
public class Product {
  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
  @SequenceGenerator(name = "product_seq", allocationSize = 1)
  private Long id;

  @NaturalId
  @Column(name = "SKU", unique = true)
  @NotBlank(message = "Product SKU cannot be null")
  private String sku;

  @Column(name = "TITLE", unique = true)
  @NotBlank(message = "Title can not be blank")
  private String title;

  @ManyToOne
  @JoinColumn(name="category_id")
  private ProductCategory category;

  @Column(name = "SUMMARY")
  @NotBlank(message = "Sort summary can not be blank")
  private String summary;

  @Column(name = "SORT_SUMMARY")
  private String sortSummary;

  @Column(name = "PRICE")
  @NotBlank(message = "Price summary can not be blank")
  private float price;

  @Column(name = "STATUS")
  private boolean status;

  @Column(name = "IMAGE")
  private String image;

  public Product(){
  }

  public Product(Long id, String sku, String title,
                 ProductCategory category, String summary, String sortSummary,
                 float price,
                 boolean status, String image) {
    this.id = id;
    this.sku = sku;
    this.title = title;
    this.category = category;
    this.summary = summary;
    this.sortSummary = sortSummary;
    this.price = price;
    this.status = status;
    this.image = image;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getSortSummary() {
    return sortSummary;
  }

  public void setSortSummary(String sortSummary) {
    this.sortSummary = sortSummary;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return Objects.equals(id, product.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
