package com.example.ecomerce.controller;

import com.example.ecomerce.dto.ProductDTO;
import com.example.ecomerce.exception.NoSuchElementFoundException;
import com.example.ecomerce.mapper.ProductMapper;
import com.example.ecomerce.model.Product;
import com.example.ecomerce.model.ProductCategory;
import com.example.ecomerce.model.payload.PagedResponseDTO;
import com.example.ecomerce.repository.ProductCategoryRepository;
import com.example.ecomerce.repository.ProductRepository;
import com.example.ecomerce.search.CustomRsqlVisitor;
import com.example.ecomerce.services.ProductService;
import com.example.ecomerce.utils.CustomMessage;
import com.example.ecomerce.utils.PaginationLinksUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/products")
@Slf4j(topic = "PRODUCT_CONTROLLER")
public class ProductController {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private PaginationLinksUtils paginationLinksUtils;
  @Autowired
  private CustomMessage customMessage;

  @GetMapping
  public ResponseEntity<PagedResponseDTO<Object>> getProducts(
      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
      @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
      @RequestParam(value = "search",required = false) String search,
      HttpServletRequest request){

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<Product> products = null;
    if (ObjectUtils.isEmpty(search)){
      products = productService.findAll(pageable);
    }else{
      Node rootNode = new RSQLParser().parse(search);
      Specification<Product> spec = rootNode.accept(new CustomRsqlVisitor<>());
      products = productRepository.findAll(spec, pageable);
    }
    if(products.hasContent()){
      UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
          .fromHttpUrl(request.getRequestURL().toString());
      return ResponseEntity.ok()
          .header("link", paginationLinksUtils.createLinkHeader(products, uriComponentsBuilder))
          .body(PagedResponseDTO.builder()
                    .total(products.getTotalElements())
                    .pages(products.getTotalPages())
                    .current(products.getNumber())
                    .data(products.toList())
                    .build());
    }else {
      return ResponseEntity.ok().body(PagedResponseDTO.builder()
                                          .total(0L)
                                          .pages(0)
                                          .current(pageNumber)
                                          .data(new ArrayList<>())
                                          .build());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long productId){
    Product product =
        productService.findById(productId).orElseThrow(() -> new NoSuchElementFoundException(customMessage.getLocalMessage("item.absent", productId.toString())));
    ProductDTO productDto = productService.createProductDTO(product);
    return new ResponseEntity<>(productDto, HttpStatus.OK);
  }
  
  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO request){
    Product product = productService.save(productService.createProduct(request));
    ProductDTO productCreateDto = productService.createProductDTO(product);
    return new ResponseEntity<>(productCreateDto, HttpStatus.CREATED);
  }

}
