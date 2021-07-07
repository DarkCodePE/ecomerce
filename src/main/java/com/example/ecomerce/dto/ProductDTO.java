package com.example.ecomerce.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "Registration Product Request or Response", description = "The "
    + "registration "
    + "request or response "
    + "payload")
@Getter
@Setter
public class ProductDTO {

  @NotBlank(message = "Product id cannot be blank")
  @ApiModelProperty(value = "Product Id", required = true, dataType = "string", allowableValues = "Non empty string")
  private String productId;

  @NotBlank(message = "Registration Title can be null but not blank")
  @ApiModelProperty(value = "A valid title", allowableValues = "NonEmpty String")
  private String title;

  @NotBlank(message = "Registration Price can be null but not blank")
  @ApiModelProperty(value = "A valid price", allowableValues = "NonEmpty String")
  private float price;

  @NotBlank(message = "Registration Image can be null but not blank")
  @ApiModelProperty(value = "A valid image", allowableValues = "NonEmpty String")

  private String image;
  @NotBlank(message = "Registration Product Category can be null but not blank")
  @ApiModelProperty(value = "A valid image", allowableValues = "NonEmpty String")
  private String productCategory;
}
