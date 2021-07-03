package com.example.ecomerce.cache;

import com.example.ecomerce.event.OnUserLogoutSuccessEvent;
import com.example.ecomerce.security.JwtTokenProvider;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j(topic = "LOGGED_OUT_JWT_TOKEN_CACHE")
public class LoggedOutJwtTokenCache {
  private final ExpiringMap<String, OnUserLogoutSuccessEvent> tokenEventMap;
  private final JwtTokenProvider tokenProvider;

  @Autowired
  public LoggedOutJwtTokenCache(@Value("${app.cache.logoutToken.maxSize}") int maxSize, JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
    this.tokenEventMap = ExpiringMap.builder()
        .variableExpiration()
        .maxSize(maxSize)
        .build();
  }

  public void markLogoutEventForToken(OnUserLogoutSuccessEvent event) {
    String token = event.getToken();
    if (tokenEventMap.containsKey(token)) {
      log.info(String.format("Log out token for user [%s] is already present in the "
                                 + "cache", event.getUserEmail()));

    } else {
      Date tokenExpiryDate = tokenProvider.getTokenExpiryFromJWT(token);
      long ttlForToken = getTTLForToken(tokenExpiryDate);
      log.info(String.format("Logout token cache set for [%s] with a TTL of [%s] seconds. Token is due expiry at [%s]", event.getUserEmail(), ttlForToken, tokenExpiryDate));
      tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS);
    }
  }

  public OnUserLogoutSuccessEvent getLogoutEventForToken(String token) {
    return tokenEventMap.get(token);
  }

  private long getTTLForToken(Date date) {
    long secondAtExpiry = date.toInstant().getEpochSecond();
    long secondAtLogout = Instant.now().getEpochSecond();
    return Math.max(0, secondAtExpiry - secondAtLogout);
  }

}
