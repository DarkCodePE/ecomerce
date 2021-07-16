package com.example.ecomerce.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity(name = "password_reset_token")
@Table(name="password_reset_tokens")
public class PasswordResetToken {
  @Id
  @Column(name = "token_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pwd_reset_token_seq")
  @SequenceGenerator(name = "pwd_reset_token_seq", allocationSize = 1)
  private Long id;

  @NaturalId
  @Column(name = "token_name", nullable = false, unique = true)
  private String token;

  @Column(name = "expiry_dt", nullable = false)
  private Instant expiryDate;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  public PasswordResetToken(Long id, String token, Instant expiryDate, User user) {
    this.id = id;
    this.token = token;
    this.expiryDate = expiryDate;
    this.user = user;
  }

  public PasswordResetToken() {
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
