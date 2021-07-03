package com.example.ecomerce.services;

import com.example.ecomerce.model.CustomUserDetails;
import com.example.ecomerce.model.User;
import com.example.ecomerce.repository.UserRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "CUSTOM_USER_DETAILS_SERVICE")
public class CustomUserDetailsService implements
    UserDetailsService {

  @Autowired
  public UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.info("EMAIL_2 {}", email);
    Optional<User> dbUser = userRepository.findByEmail(email);
    User user = userRepository.findByEmail(email).orElseThrow(() ->new RuntimeException(
        "NOT MAIL"));
    log.info("Fetched user password : " + dbUser.map(User::getPassword)+ " by " + email);
    log.info("Fetched user : " + dbUser.map(User::getEmail) + " by " + email);
    CustomUserDetails customUserDetails = CustomUserDetails.create(user);
    log.info("customUserDetails: {}", customUserDetails.getUsername());
    return customUserDetails;
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    Optional<User> dbUser = userRepository.findById(id);
    log.info("Fetched user : " + dbUser + " by " + id);
    return dbUser.map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
  }
}
