package com.example.ecomerce.repository;

import com.example.ecomerce.model.token.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  @Override
  Optional<RefreshToken> findById(Long id);

  Optional<RefreshToken> findByToken(String token);
}
