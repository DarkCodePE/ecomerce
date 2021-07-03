package com.example.ecomerce.mapper;

import com.example.ecomerce.annotation.EncodedMapping;
import com.example.ecomerce.dto.UserDTO;
import com.example.ecomerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper  {
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "password", target = "password", qualifiedBy = EncodedMapping.class)
  User map(UserDTO userDTO);
  UserDTO mapToDto(User user);
}
