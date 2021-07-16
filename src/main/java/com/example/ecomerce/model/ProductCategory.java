package com.example.ecomerce.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="product_categories")
public class ProductCategory {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
}
