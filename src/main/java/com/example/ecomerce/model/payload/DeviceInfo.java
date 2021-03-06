package com.example.ecomerce.model.payload;

import io.swagger.annotations.ApiModelProperty;

import com.example.ecomerce.model.DeviceType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DeviceInfo {

  @NotBlank(message = "Device id cannot be blank")
  @ApiModelProperty(value = "Device Id", required = true, dataType = "string", allowableValues = "Non empty string")
  private String deviceId;

  @NotNull(message = "Device type cannot be null")
  @ApiModelProperty(value = "Device type Android/iOS", required = true, dataType = "string", allowableValues =
      "DEVICE_TYPE_MOBILE, DEVICE_TYPE_TABLET, DEVICE_TYPE_DESKTOP")
  private DeviceType deviceType;

  @ApiModelProperty(value = "Device operation system", dataType = "string",
      allowableValues = "Non empty string")
  private String deviceSystem;

  @NotNull(message = "Device notification token can not null")
  @ApiModelProperty(value = "Device notification id", dataType = "string", allowableValues = "Non empty string")
  private String notificationToken;

  @NotBlank(message = "Specify whether Device has to be registered enable refresh token")
  @ApiModelProperty(value = "enabled refresh token", dataType = "string",
      allowableValues = "Non empty "
      + "string")
  private Boolean isRefreshActive;

  public DeviceInfo() {
  }

  public DeviceInfo(String deviceId, DeviceType deviceType, String deviceSystem,
                    String notificationToken, Boolean isRefreshActive) {
    this.deviceId = deviceId;
    this.deviceType = deviceType;
    this.deviceSystem = deviceSystem;
    this.notificationToken = notificationToken;
    this.isRefreshActive = isRefreshActive;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public String getNotificationToken() {
    return notificationToken;
  }

  public void setNotificationToken(String notificationToken) {
    this.notificationToken = notificationToken;
  }

  public String getDeviceSystem() {
    return deviceSystem;
  }

  public void setDeviceSystem(String deviceSystem) {
    this.deviceSystem = deviceSystem;
  }

  public Boolean getRefreshActive() {
    return isRefreshActive;
  }

  public void setRefreshActive(Boolean refreshActive) {
    isRefreshActive = refreshActive;
  }
}
