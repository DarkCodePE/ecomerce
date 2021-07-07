package com.example.ecomerce.model.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PagedResponseDTO<E> {
  private Long total;
  private Integer pages;
  private Integer current;
  private E data;
}
