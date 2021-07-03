package com.example.ecomerce.services;

import com.example.ecomerce.exception.TokenRefreshException;
import com.example.ecomerce.model.token.RefreshToken;
import com.example.ecomerce.repository.RefreshTokenRepository;
import com.example.ecomerce.services.base.BaseService;
import com.example.ecomerce.utils.Util;
import java.time.Instant;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j(topic = "REFRESH_TOKEN_SERVICE")
@Service
public class RefreshTokenService extends BaseService<RefreshToken, Long, RefreshTokenRepository> {
  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${app.token.refresh.duration}")
  private Long refreshTokenDurationMs;

  public RefreshTokenService(
      RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  /**
   * Find a refresh token based on the natural id i.e the token itself
   */
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  /**
   * Persist the updated refreshToken instance to database
   */
  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    log.info("REFRESH_TOKEN_TEST: {}", refreshToken.getToken());
    return repository.save(refreshToken);
  }

  /**
   * Creates and returns a new refresh token
   */
  public RefreshToken createRefreshToken() {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(Util.generateRandomUuid());
    refreshToken.setRefreshCount(0L);
    return refreshToken;
  }

  /**
   * Verify whether the token provided has expired or not on the basis of the current
   * server time and/or throw error otherwise
   */
  public void verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      throw new TokenRefreshException("error.refresh,token.expiration", token.getToken());
    }
  }

  /**
   * Delete the refresh token associated with the user device
   */
  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  /**
   * Increase the count of the token usage in the database. Useful for
   * audit purposes
   */
  public void increaseCount(RefreshToken refreshToken) {
    refreshToken.incrementRefreshCount();
    save(refreshToken);
  }
}
