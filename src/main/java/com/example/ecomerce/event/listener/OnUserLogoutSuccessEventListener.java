package com.example.ecomerce.event.listener;

import com.example.ecomerce.cache.LoggedOutJwtTokenCache;
import com.example.ecomerce.event.OnUserLogoutSuccessEvent;
import com.example.ecomerce.model.payload.DeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "ON_USER_LOGOUT_SUCCESS_EVENT_LISTENER")
@Component
public class OnUserLogoutSuccessEventListener implements
    ApplicationListener<OnUserLogoutSuccessEvent> {
  private final LoggedOutJwtTokenCache tokenCache;

  @Autowired
  public OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
    this.tokenCache = tokenCache;
  }

  public void onApplicationEvent(OnUserLogoutSuccessEvent event) {
    if (null != event) {
      DeviceInfo deviceInfo = event.getLogOutRequest().getDeviceInfo();
      log.info(String.format("Log out success event received for user [%s] for device [%s]", event.getUserEmail(), deviceInfo));
      tokenCache.markLogoutEventForToken(event);
    }
  }
}
