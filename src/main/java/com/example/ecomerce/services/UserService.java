package com.example.ecomerce.services;

import com.example.ecomerce.annotation.CurrentUser;
import com.example.ecomerce.dto.UserDTO;
import com.example.ecomerce.exception.UserLogoutException;
import com.example.ecomerce.mapper.UserMapper;
import com.example.ecomerce.model.CustomUserDetails;
import com.example.ecomerce.model.Role;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.UserDevice;
import com.example.ecomerce.model.payload.LogOutRequest;
import com.example.ecomerce.model.payload.RegistrationRequest;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.services.base.BaseService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "USER_SERVICE")
public class UserService extends BaseService<User, Long, UserRepository> {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoleService roleService;
  private final UserDeviceService userDeviceService;
  private final RefreshTokenService refreshTokenService;

  @Autowired
  public UserService(UserRepository userRepository,
      UserMapper userMapper, RoleService roleService,
      UserDeviceService userDeviceService,
      RefreshTokenService refreshTokenService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.roleService = roleService;
    this.userDeviceService = userDeviceService;
    this.refreshTokenService = refreshTokenService;
  }

  /**
   * Finds a user in the database by email
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Find a user in db by id.
   */
  @Override
  public Optional<User> findById(Long Id) {
    return repository.findById(Id);
  }

  /**
   * Save the user to the database
   */
  @Override
  public User save(User user) {
    return repository.save(user);
  }

  /**
   * Check is the user exists given the email: naturalId
   */
  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * Creates a new user from the registration request
   */
  public User createUser(UserDTO registerRequest) {
    return userMapper.map(registerRequest);
  }
  /**
   * Performs a quick check to see what roles the new user could be assigned to.
   *
   * @return list of roles for the new user
   */
    public Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
    Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
    if (!Boolean.TRUE.equals(isToBeMadeAdmin)) {
      newUserRoles.removeIf(Role::isAdminRole);
    }
      log.info("Setting user roles: " + roleService.findAll());
    log.info("Setting user roles: " + newUserRoles);
    return newUserRoles;
  }
  /**
   * Log the given user out and delete the refresh token associated with it. If no device
   * id is found matching the database for the given user, throw a log out exception.
   */
  public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
    String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
    UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
        .filter(device -> device.getDeviceId().equals(deviceId))
        .orElseThrow(() -> new UserLogoutException("error.device.id.notfound"));
    log.info("Removing refresh token associated with device [" + userDevice + "]");
    refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
  }
}
