package com.example.ecomerce.mapper;

import com.example.ecomerce.dto.ProductDTO;
import com.example.ecomerce.model.Product;
import com.example.ecomerce.model.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", source = "productCategory")
  Product map(ProductDTO productDTO, ProductCategory productCategory);
  ProductDTO mapToDto(Product product);
}
