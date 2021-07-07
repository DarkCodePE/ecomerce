package com.example.ecomerce.services;

import com.example.ecomerce.dto.ProductDTO;
import com.example.ecomerce.exception.NoSuchElementFoundException;
import com.example.ecomerce.mapper.ProductMapper;
import com.example.ecomerce.model.Product;
import com.example.ecomerce.model.ProductCategory;
import com.example.ecomerce.repository.ProductCategoryRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.services.base.BaseService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "PRODUCT_SERVICE")
public class ProductService extends BaseService<Product, Long, ProductRepository> {
  private final ProductMapper productMapper;
  private final ProductCategoryRepository productCategoryRepository;

  @Autowired
  public ProductService(ProductMapper productMapper,
                        ProductCategoryRepository productCategoryRepository) {
    this.productMapper = productMapper;
    this.productCategoryRepository = productCategoryRepository;
  }
  /**
   * Finds a user in the database by sku
   */
  public Optional<Product> findBySku(String sku) {
    return repository.findBySku(sku);
  }
  /**
   * Creates a new product from the registration request
   */
  public Product createProduct(ProductDTO productDTO){
    ProductCategory productCategory =
            productCategoryRepository.findByName(productDTO.getProductCategory()).orElseThrow(() -> new NoSuchElementFoundException("item.absent", productDTO.getProductCategory()));
    return productMapper.map(productDTO, productCategory);
  }
  /**
   * Creates a new productDTO from the registration request
   */
  public ProductDTO createProductDTO(Product product){
    return productMapper.mapToDto(product);
  }
}
