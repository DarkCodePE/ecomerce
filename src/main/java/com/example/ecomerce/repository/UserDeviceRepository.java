package com.example.ecomerce.repository;

import com.example.ecomerce.model.UserDevice;
import com.example.ecomerce.model.token.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
  Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

  Optional<UserDevice> findByUserId(Long userId);
}
