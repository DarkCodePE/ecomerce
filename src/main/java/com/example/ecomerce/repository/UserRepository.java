package com.example.ecomerce.repository;

import com.example.ecomerce.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

}
