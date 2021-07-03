package com.example.ecomerce.mapper;

import com.example.ecomerce.model.User;
import com.example.ecomerce.model.UserDevice;
import com.example.ecomerce.model.payload.DeviceInfo;
import com.example.ecomerce.model.token.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDeviceMapper {
   @Mapping(target = "id", ignore = true)
   @Mapping(target = "user", source = "user")
   @Mapping(target = "refreshToken", source = "refreshToken")
   UserDevice map(DeviceInfo deviceInfo, User user, RefreshToken refreshToken);
   @Mapping(target = "deviceId", source = "id")
   DeviceInfo mapToDto(UserDevice userDevice);
}
