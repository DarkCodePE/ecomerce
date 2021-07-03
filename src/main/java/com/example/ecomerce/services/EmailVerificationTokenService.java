package com.example.ecomerce.services;

import com.example.ecomerce.exception.InvalidTokenRequestException;
import com.example.ecomerce.model.TokenStatus;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.token.EmailVerificationToken;
import com.example.ecomerce.repository.EmailVerificationTokenRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j(topic = "EMAIL_VERIFICATION_TOKEN_SERVICE")
@Service
public class EmailVerificationTokenService {
  private final EmailVerificationTokenRepository emailVerificationTokenRepository;
  @Value("${app.token.email.verification.duration}")
  private Long emailVerificationTokenExpiryDuration;

  @Autowired
  public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
    this.emailVerificationTokenRepository = emailVerificationTokenRepository;
  }

  /**
   * Create an email verification token and persist it in the database which will be
   * verified by the user
   */
  public void createVerificationToken(User user, String token) {
    EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
    emailVerificationToken.setToken(token);
    emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
    emailVerificationToken.setUser(user);
    emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
    log.info("Generated Email verification token [" + emailVerificationToken + "]");
    emailVerificationTokenRepository.save(emailVerificationToken);
  }
  /**
   * Updates an existing token in the database with a new expiration
   */
  public EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken) {
    existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
    existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
    log.info("Updated Email verification token [" + existingToken + "]");
    return save(existingToken);
  }

  /**
   * Finds an email verification token by the @NaturalId token
   */
  public Optional<EmailVerificationToken> findByToken(String token) {
    return emailVerificationTokenRepository.findByToken(token);
  }

  /**
   * Saves an email verification token in the repository
   */
  public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
    return emailVerificationTokenRepository.save(emailVerificationToken);
  }

  /**
   * Generates a new random UUID to be used as the token for email verification
   */
  public String generateNewToken() {
    return UUID.randomUUID().toString();
  }

  /**
   * Verify whether the token provided has expired or not on the basis of the current
   * server time and/or throw error otherwise
   */
  public void verifyExpiration(EmailVerificationToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      throw new InvalidTokenRequestException("Email Verification Token", token.getToken(), "Expired token. Please issue a new request");
    }
  }
}
