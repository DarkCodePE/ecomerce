package com.example.ecomerce.dto;

import com.example.ecomerce.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Registration Request or Response", description = "The registration "
    + "request or response "
    + "payload")

public class UserDTO {
  //@JsonView(value = {View.UserView.Request.class})
  @NotBlank(message = "Registration firstname can be null but not blank")
  @ApiModelProperty(value = "A valid firstname", allowableValues = "NonEmpty String")
  private String firstName;

  //@JsonView(value = {View.UserView.Request.class})
  @NotBlank(message = "Registration lastname can be null but not blank")
  @ApiModelProperty(value = "A valid lastname", allowableValues = "NonEmpty String")
  private String lastName;

  //@JsonView(value = {View.UserView.Request.class})
  @NotBlank(message = "Registration username can be null but not blank")
  @ApiModelProperty(value = "A valid username", allowableValues = "NonEmpty String")
  private String username;

  //@JsonView(value = {View.UserView.Request.class})
  @NotBlank(message = "Registration email can be null but not blank")
  @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
  private String email;

  //@JsonView(value = {View.UserView.Request.class})
  @NotNull(message = "Registration password cannot be null")
  @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
  private String password;

  //@JsonView(value = {View.UserView.Request.class})
  @NotNull(message = "Specify whether the user has to be registered as an admin or not")
  @ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
      dataType = "boolean", allowableValues = "true, false")
  private Boolean registerAsAdmin;

  //@JsonView(value = {View.UserView.Response.class})
  @ApiModelProperty(value = "Flag denoting whether the user is active or not",
      dataType = "boolean", allowableValues = "true, false")
  private Boolean active;

  //@JsonView(value = {View.UserView.Response.class})
  @ApiModelProperty(value = "Flag denoting whether the user email is verified or not",
      dataType = "boolean", allowableValues = "true, false")
  private Boolean isEmailVerified;

  public UserDTO(String firstName, String lastName, String username, String email,
                             String password, Boolean registerAsAdmin) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.registerAsAdmin = registerAsAdmin;
    this.active = true;
    this.isEmailVerified = false;
  }

  public UserDTO() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Boolean getEmailVerified() {
    return isEmailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    isEmailVerified = emailVerified;
  }
}
