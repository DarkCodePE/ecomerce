package com.example.ecomerce.services;

import com.example.ecomerce.dto.UserDTO;
import com.example.ecomerce.exception.PasswordResetLinkException;
import com.example.ecomerce.exception.ResourceAlreadyInUseException;
import com.example.ecomerce.exception.ResourceNotFoundException;
import com.example.ecomerce.exception.TokenRefreshException;
import com.example.ecomerce.exception.UpdatePasswordException;
import com.example.ecomerce.model.CustomUserDetails;
import com.example.ecomerce.model.PasswordResetToken;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.UserDevice;
import com.example.ecomerce.model.payload.PasswordResetLinkRequest;
import com.example.ecomerce.model.payload.TokenRefreshRequest;
import com.example.ecomerce.model.token.EmailVerificationToken;
import com.example.ecomerce.model.payload.LoginRequest;
import com.example.ecomerce.model.payload.PasswordResetRequest;
import com.example.ecomerce.model.payload.RegistrationRequest;
import com.example.ecomerce.model.payload.UpdatePasswordRequest;
import com.example.ecomerce.model.token.RefreshToken;
import com.example.ecomerce.security.JwtTokenProvider;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j(topic = "AUTH_SERVICE")
@Service
public class AuthService {

  private final UserService userService;
  private final JwtTokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final EmailVerificationTokenService emailVerificationTokenService;
  private final UserDeviceService userDeviceService;
  private final PasswordResetTokenService passwordResetTokenService;

  @Autowired
  public AuthService(UserService userService,
      JwtTokenProvider tokenProvider,
      RefreshTokenService refreshTokenService,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      EmailVerificationTokenService emailVerificationTokenService,
      UserDeviceService userDeviceService,
      PasswordResetTokenService passwordResetTokenService) {
    this.userService = userService;
    this.tokenProvider = tokenProvider;
    this.refreshTokenService = refreshTokenService;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.emailVerificationTokenService = emailVerificationTokenService;
    this.userDeviceService = userDeviceService;
    this.passwordResetTokenService = passwordResetTokenService;
  }

  /**
   * Registers a new user in the database by performing a series of quick checks.
   *
   * @return A user object if successfully created
   */
  @Transactional
  public Optional<User> registerUser(UserDTO newRegistrationRequest) {
    String newRegistrationRequestEmail = newRegistrationRequest.getEmail();

    if (Boolean.TRUE.equals(emailAlreadyExists(newRegistrationRequestEmail))) {
      log.error("Email already exists: " + newRegistrationRequestEmail);
      throw new ResourceAlreadyInUseException("error.email.already.use", newRegistrationRequestEmail);
    }
    log.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
    User newUser = userService.createUser(newRegistrationRequest);
    newUser.setActive(true);
    newUser.setEmailVerified(false);
    newUser.setRoles(userService.getRolesForNewUser(false));
    newUser.setUsername("orlando");
    log.info("user_name: {}", newUser.getUsername());
    User registeredNewUser = userService.save(newUser);
    return Optional.ofNullable(registeredNewUser);
  }
  /**
   * Checks if the given email already exists in the database repository or not
   *
   * @return true if the email exists else false
   */
  public Boolean emailAlreadyExists(String email) {
    return userService.existsByEmail(email);
  }
  /**
   * Authenticate user and log them in given a loginRequest
   */
  public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
    log.info("LOGIN_REQUEST_device: {}", loginRequest.getDeviceInfo().getDeviceSystem());
    log.info("LOGIN REQUEST: {}", loginRequest.getEmail());
    log.info("LOGIN REQUEST: {}", loginRequest.getPassword());
    return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
        loginRequest.getPassword())));
  }

  /**
   * Confirms the user verification based on the token expiry and mark the user as active.
   * If user is already verified, save the unnecessary database calls.
   */
  public Optional<User> confirmEmailRegistration(String emailToken) {
    log.info("emailToken: {}", emailToken);
    EmailVerificationToken emailVerificationToken =
        emailVerificationTokenService.findByToken(emailToken)
        .orElseThrow(() -> new ResourceNotFoundException("error.resource.notfound", emailToken));

    User registeredUser = emailVerificationToken.getUser();
    if (Boolean.TRUE.equals(registeredUser.getEmailVerified())) {
      log.info("User [" + emailToken + "] already registered.");
      return Optional.of(registeredUser);
    }

    emailVerificationTokenService.verifyExpiration(emailVerificationToken);
    emailVerificationToken.setConfirmedStatus();
    emailVerificationTokenService.save(emailVerificationToken);

    registeredUser.markVerificationConfirmed();
    userService.save(registeredUser);
    return Optional.of(registeredUser);
  }

  /**
   * Attempt to regenerate a new email verification token given a valid
   * previous expired token. If the previous token is valid, increase its expiry
   * else update the token value and add a new expiration.
   */
  public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
    EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
        .orElseThrow(() -> new ResourceNotFoundException("error.resource.notfound", existingToken));

    if (Boolean.TRUE.equals(emailVerificationToken.getUser().getEmailVerified())) {
      return Optional.empty();
    }
    return Optional.ofNullable(emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));
  }
  /**
   * Validates the password of the current logged in user with the given password
   */
  private Boolean currentPasswordMatches(User currentUser, String password) {
    return passwordEncoder.matches(password, currentUser.getPassword());
  }

  /**
   * Updates the password of the current logged in user
   */
  public Optional<User> updatePassword(CustomUserDetails customUserDetails,
      UpdatePasswordRequest updatePasswordRequest) {
    String email = customUserDetails.getEmail();
    User currentUser = userService.findByEmail(email)
        .orElseThrow(() -> new UpdatePasswordException("error.update.password" ,"No matching user found", email));

    if (!Boolean.TRUE.equals(currentPasswordMatches(currentUser,
                                        updatePasswordRequest.getOldPassword()))) {
      log.info("Current password is invalid for [" + currentUser.getPassword() + "]");
      throw new UpdatePasswordException("error.update.password", "Invalid current password", currentUser.getEmail());
    }
    String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
    currentUser.setPassword(newPassword);
    userService.save(currentUser);
    return Optional.of(currentUser);
  }

  /**
   * Generates a JWT token for the validated client
   */
  public String generateToken(CustomUserDetails customUserDetails) {
    return tokenProvider.generateToken(customUserDetails);
  }

  /**
   * Generates a JWT token for the validated client by userId
   */
  public String generateTokenFromUserId(Long userId) {
    return tokenProvider.generateTokenFromUserId(userId);
  }

  /**
   * Creates and persists the refresh token for the user device. If device exists
   * already, we don't care. Unused devices with expired tokens should be cleaned
   * with a cron job. The generated token would be encapsulated within the jwt.
   * Remove the existing refresh token as the old one should not remain valid.
   */
  @Transactional
  public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginRequest loginRequest) {
    User currentUser = (User) authentication.getPrincipal();
    userDeviceService.findByUserId(currentUser.getId())
        .map(UserDevice::getRefreshToken)
        .map(RefreshToken::getId)
        .ifPresent(refreshTokenService::deleteById);
    RefreshToken refreshToken = refreshTokenService.createRefreshToken();
    UserDevice userDevice =
        userDeviceService.createUserDevice(loginRequest.getDeviceInfo(), currentUser, refreshToken);
    log.info("USER_DEVICE_DATA_CURRENT: {}", currentUser.getEmail());
    //userDevice.setRefreshToken(refreshToken);
    refreshToken.setUserDevice(userDevice);
    log.info("REFRESH_TOKEN: {}", refreshToken.getToken());
    refreshToken = refreshTokenService.save(refreshToken);
    return Optional.ofNullable(refreshToken);
  }

  /**
   * Refresh the expired jwt token using a refresh token and device info. The
   * * refresh token is mapped to a specific device and if it is unexpired, can help
   * * generate a new jwt. If the refresh token is inactive for a device or it is expired,
   * * throw appropriate errors.
   */
  public Optional<String> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
    String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

    return Optional.of(refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshToken -> {
          refreshTokenService.verifyExpiration(refreshToken);
          userDeviceService.verifyRefreshAvailability(refreshToken);
          refreshTokenService.increaseCount(refreshToken);
          return refreshToken;
        })
        .map(RefreshToken::getUserDevice)
        .map(UserDevice::getUser)
        .map(CustomUserDetails::new)
        .map(this::generateToken))
        .orElseThrow(() -> new TokenRefreshException("error.refresh.token", "Missing refresh token in database.Please login again", requestRefreshToken));
  }

  /**
   * Generates a password reset token from the given reset request
   */
  public Optional<PasswordResetToken> generatePasswordResetToken(
      PasswordResetLinkRequest passwordResetLinkRequest) {
    String email = passwordResetLinkRequest.getEmail();
    return userService.findByEmail(email)
        .map(user -> {
          PasswordResetToken passwordResetToken = passwordResetTokenService.createToken();
          passwordResetToken.setUser(user);
          passwordResetTokenService.save(passwordResetToken);
          return Optional.of(passwordResetToken);
        })
        .orElseThrow(() -> new PasswordResetLinkException("error.matching.user.notfound", email));
  }

  /**
   * Reset a password given a reset request and return the updated user
   */
  public Optional<User> resetPassword(PasswordResetRequest passwordResetRequest) {
    String token = passwordResetRequest.getToken();
    PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Password Reset Token", "Token Id", token));

    passwordResetTokenService.verifyExpiration(passwordResetToken);
    final String encodedPassword = passwordEncoder.encode(passwordResetRequest.getPassword());

    return Optional.of(passwordResetToken)
        .map(PasswordResetToken::getUser)
        .map(user -> {
          user.setPassword(encodedPassword);
          userService.save(user);
          return user;
        });
  }
}
