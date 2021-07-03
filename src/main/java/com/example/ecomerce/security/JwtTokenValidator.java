package com.example.ecomerce.security;

import com.example.ecomerce.cache.LoggedOutJwtTokenCache;
import com.example.ecomerce.event.OnUserLogoutSuccessEvent;
import com.example.ecomerce.exception.InvalidTokenRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "JWT_TOKEN_VALIDATOR")
@Component
public class JwtTokenValidator {
  private final String jwtSecret;
  private final LoggedOutJwtTokenCache loggedOutTokenCache;

  public JwtTokenValidator(@Value("${app.jwt.secret}") String jwtSecret,
      LoggedOutJwtTokenCache loggedOutTokenCache) {
    this.jwtSecret = jwtSecret;
    this.loggedOutTokenCache = loggedOutTokenCache;
  }

  /**
   * Validates if a token satisfies the following properties
   * - Signature is not malformed
   * - Token hasn't expired
   * - Token is supported
   * - Token has not recently been logged out.
   */
  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);

    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
      throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");

    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
      throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");

    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
      throw new InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required");

    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
      throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");

    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
      throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
    }
    validateTokenIsNotForALoggedOutDevice(authToken);
    return true;
  }

  private void validateTokenIsNotForALoggedOutDevice(String authToken) {
    OnUserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutTokenCache.getLogoutEventForToken(authToken);
    if (previouslyLoggedOutEvent != null) {
      String userEmail = previouslyLoggedOutEvent.getUserEmail();
      Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
      String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]. Please login again", userEmail, logoutEventDate);
      throw new InvalidTokenRequestException("JWT", authToken, errorMessage);
    }
  }
}
