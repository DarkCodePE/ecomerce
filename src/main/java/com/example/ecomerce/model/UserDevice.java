package com.example.ecomerce.model;

import com.example.ecomerce.model.token.RefreshToken;
import javax.persistence.*;

@Entity(name = "user_device")
@Table(name="user_devices")
public class UserDevice {
  @Id
  @Column(name = "user_device_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_device_seq")
  @SequenceGenerator(name = "user_device_seq", allocationSize = 1)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "device_type")
  @Enumerated(value = EnumType.STRING)
  private DeviceType deviceType;

  @Column(name = "notification_type")
  private String notificationToken;

  @Column(name = "device_id", nullable = false)
  private String deviceId;

  @OneToOne(mappedBy = "userDevice", orphanRemoval = true,
      cascade = CascadeType.ALL)
  private RefreshToken refreshToken;

  @Column(name = "IS_REFRESH_ACTIVE")
  private Boolean isRefreshActive;

  public UserDevice() {
  }

  public UserDevice(Long id, User user, DeviceType deviceType, String notificationToken, String deviceId,
      RefreshToken refreshToken, Boolean isRefreshActive) {
    this.id = id;
    this.user = user;
    this.deviceType = deviceType;
    this.notificationToken = notificationToken;
    this.deviceId = deviceId;
    this.refreshToken = refreshToken;
    this.isRefreshActive = isRefreshActive;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Boolean getRefreshActive() {
    return isRefreshActive;
  }

  public void setRefreshActive(Boolean refreshActive) {
    isRefreshActive = refreshActive;
  }
}
