package com.example.ecomerce.services;

import com.example.ecomerce.model.Role;
import com.example.ecomerce.repository.RoleRepository;
import com.example.ecomerce.services.base.BaseService;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends BaseService<Role, Long, RoleRepository> {
  /**
   * Find all roles from the database
   */
  public List<Role> findAll() {
    return repository.findAll();
  }
}
