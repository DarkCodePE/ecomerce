package com.example.ecomerce.security.oauth2.user;

import com.example.ecomerce.exception.OAuth2AuthenticationProcessingException;
import com.example.ecomerce.model.AuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {
  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
      return new FacebookOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
      return new GithubOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationProcessingException("error.oauth2.authentication", registrationId);
    }
  }
}
