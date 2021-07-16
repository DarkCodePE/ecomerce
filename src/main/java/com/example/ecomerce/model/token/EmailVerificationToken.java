package com.example.ecomerce.model.token;

import com.example.ecomerce.model.TokenStatus;
import com.example.ecomerce.model.User;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity(name = "email_verification_token")
public class EmailVerificationToken{
  @Id
  @Column(name = "token_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_token_seq")
  @SequenceGenerator(name = "email_token_seq", allocationSize = 1)
  private Long id;

  @Column(name = "token", nullable = false, unique = true)
  private String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @Column(name = "token_status")
  @Enumerated(EnumType.STRING)
  private TokenStatus tokenStatus;

  @Column(name = "expiry_dt", nullable = false)
  private Instant expiryDate;
  public EmailVerificationToken() {
  }

  public EmailVerificationToken(Long id, String token, User user, TokenStatus tokenStatus, Instant expiryDate) {
    this.id = id;
    this.token = token;
    this.user = user;
    this.tokenStatus = tokenStatus;
    this.expiryDate = expiryDate;
  }

  public void setConfirmedStatus() {
    setTokenStatus(TokenStatus.STATUS_CONFIRMED);
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

  public TokenStatus getTokenStatus() {
    return tokenStatus;
  }

  public void setTokenStatus(TokenStatus tokenStatus) {
    this.tokenStatus = tokenStatus;
  }

}
