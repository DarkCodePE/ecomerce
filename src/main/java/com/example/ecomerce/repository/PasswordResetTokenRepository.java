package com.example.ecomerce.repository;

import com.example.ecomerce.model.PasswordResetToken;
import com.example.ecomerce.model.payload.PasswordResetRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByToken(String token);
}
