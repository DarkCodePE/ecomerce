package com.example.ecomerce.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomUserDetails extends User implements UserDetails, OAuth2User {

  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  public CustomUserDetails(final User user) {
    super(user);
  }

  public CustomUserDetails(
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes) {
    this.authorities = authorities;
    this.attributes = attributes;
  }

  public CustomUserDetails(User user,
      Collection<? extends GrantedAuthority> authorities) {
    super(user);
    this.authorities = authorities;
  }

  public static CustomUserDetails create(User user) {
    List<GrantedAuthority> authorities = Collections.
        singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    return new CustomUserDetails(
       user, authorities
    );
  }

  public static CustomUserDetails create(User user, Map<String, Object> attributes) {
    CustomUserDetails userPrincipal = CustomUserDetails.create(user);
    userPrincipal.setAttributes(attributes);
    return userPrincipal;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getName() {
    return String.valueOf(getId());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return super.getPassword();
  }

  @Override
  public String getUsername() {
    return super.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return super.getActive();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return super.getEmailVerified();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    CustomUserDetails that = (CustomUserDetails) obj;
    return Objects.equals(getId(), that.getId());
  }
}
