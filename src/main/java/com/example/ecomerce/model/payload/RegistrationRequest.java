package com.example.ecomerce.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Registration Request", description = "The registration request payload")
public class RegistrationRequest {

  @NotBlank(message = "Registration firstname can be null but not blank")
  @ApiModelProperty(value = "A valid firstname", allowableValues = "NonEmpty String")
  private String firstName;

  @NotBlank(message = "Registration lastname can be null but not blank")
  @ApiModelProperty(value = "A valid lastname", allowableValues = "NonEmpty String")
  private String lastName;

  @NotBlank(message = "Registration email can be null but not blank")
  @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
  private String email;

  @NotNull(message = "Registration password cannot be null")
  @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
  private String password;

  @NotNull(message = "Specify whether the user has to be registered as an admin or not")
  @ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
      dataType = "boolean", allowableValues = "true, false")
  private Boolean registerAsAdmin;

  public RegistrationRequest(String firstName, String lastName, String email,
      String password, Boolean registerAsAdmin) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.registerAsAdmin = registerAsAdmin;
  }

  public RegistrationRequest() {
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Boolean getRegisterAsAdmin() {
    return registerAsAdmin;
  }

  public void setRegisterAsAdmin(Boolean registerAsAdmin) {
    this.registerAsAdmin = registerAsAdmin;
  }
}
