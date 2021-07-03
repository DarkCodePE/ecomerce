package com.example.ecomerce.services;

import com.example.ecomerce.exception.TokenRefreshException;
import com.example.ecomerce.mapper.UserDeviceMapper;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.UserDevice;
import com.example.ecomerce.model.payload.DeviceInfo;
import com.example.ecomerce.model.token.RefreshToken;
import com.example.ecomerce.repository.UserDeviceRepository;
import com.example.ecomerce.services.base.BaseService;
import com.example.ecomerce.utils.CustomMessage;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeviceService extends BaseService<UserDevice, Long, UserDeviceRepository> {
  private final UserDeviceRepository userDeviceRepository;
  private final UserDeviceMapper userDeviceMapper;
  private final CustomMessage customMessage;

  @Autowired
  public UserDeviceService(
      UserDeviceRepository userDeviceRepository,
      UserDeviceMapper userDeviceMapper, CustomMessage customMessage) {
    this.userDeviceRepository = userDeviceRepository;
    this.userDeviceMapper = userDeviceMapper;
    this.customMessage = customMessage;
  }

  /**
   * Find the user device info by user id
   */
  public Optional<UserDevice> findByUserId(Long userId) {
    return userDeviceRepository.findByUserId(userId);
  }
  /**
   * Find the user device info by refresh token
   */
  public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
    return userDeviceRepository.findByRefreshToken(refreshToken);
  }
  /**
   * Creates a new user device and set the user to the current device
   */
  public UserDevice createUserDevice(DeviceInfo deviceInfo, User currentUser,
                                     RefreshToken refreshToken) {
    /*UserDevice userDevice = new UserDevice();
    userDevice.setDeviceId(deviceInfo.getDeviceId());
    userDevice.setDeviceType(deviceInfo.getDeviceType());
    userDevice.setNotificationToken(deviceInfo.getNotificationToken());
    userDevice.setRefreshActive(true);
    return userDevice;*/
    return userDeviceMapper.map(deviceInfo, currentUser, refreshToken);
  }
  /**
   * Check whether the user device corresponding to the token has refresh enabled and
   * throw appropriate errors to the client
   */
  void verifyRefreshAvailability(RefreshToken refreshToken) {
    UserDevice userDevice = findByRefreshToken(refreshToken)
        .orElseThrow(() -> new TokenRefreshException("error.device.token.notfound"));

    if (!Boolean.TRUE.equals(userDevice.getRefreshActive())) {
      throw new TokenRefreshException("error.device.token.blocked");
    }
  }
}
