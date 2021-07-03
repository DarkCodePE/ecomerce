package com.example.ecomerce.model.token;

import com.example.ecomerce.model.UserDevice;
import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.NaturalId;

@Entity(name = "REFRESH_TOKEN")
public class RefreshToken {
  @Id
  @Column(name = "TOKEN_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
  @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
  private Long id;

  @Column(name = "TOKEN", nullable = false, unique = true)
  @NaturalId(mutable = true)
  private String token;

  @OneToOne(orphanRemoval = true,
      cascade = CascadeType.ALL)
  @JoinColumn(name = "USER_DEVICE_ID", unique = true, referencedColumnName = "USER_DEVICE_ID")
  private UserDevice userDevice;

  @Column(name = "REFRESH_COUNT")
  private Long refreshCount;

  @Column(name = "EXPIRY_DT", nullable = false)
  private Instant expiryDate;

  public RefreshToken() {
  }

  public RefreshToken(Long id, String token, UserDevice userDevice, Long refreshCount, Instant expiryDate) {
    this.id = id;
    this.token = token;
    this.userDevice = userDevice;
    this.refreshCount = refreshCount;
    this.expiryDate = expiryDate;
  }

  public void incrementRefreshCount() {
    refreshCount = refreshCount + 1;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserDevice getUserDevice() {
    return userDevice;
  }

  public void setUserDevice(UserDevice userDevice) {
    this.userDevice = userDevice;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

  public Long getRefreshCount() {
    return refreshCount;
  }

  public void setRefreshCount(Long refreshCount) {
    this.refreshCount = refreshCount;
  }

}
