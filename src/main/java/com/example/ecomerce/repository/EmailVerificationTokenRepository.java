package com.example.ecomerce.repository;

import com.example.ecomerce.model.token.EmailVerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
  Optional<EmailVerificationToken> findByToken(String token);
}
