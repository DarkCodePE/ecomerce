package com.example.ecomerce.controller;

import com.example.ecomerce.model.ProductCategory;
import com.example.ecomerce.repository.ProductCategoryRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {
  @Autowired
  private ProductCategoryRepository productCategoryRepository;

  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ProductCategory> createProductCategory(@RequestBody @Valid ProductCategory request){
    ProductCategory productCategory = productCategoryRepository.save(request);
    return new ResponseEntity<>(productCategory, HttpStatus.CREATED);
  }
}

