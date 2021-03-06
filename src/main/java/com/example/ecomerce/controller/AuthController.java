package com.example.ecomerce.controller;

import com.example.ecomerce.dto.UserDTO;
import com.example.ecomerce.event.OnGenerateResetLinkEvent;
import com.example.ecomerce.event.OnRegenerateEmailVerificationEvent;
import com.example.ecomerce.event.OnUserAccountChangeEvent;
import com.example.ecomerce.event.OnUserRegistrationCompleteEvent;
import com.example.ecomerce.exception.InvalidTokenRequestException;
import com.example.ecomerce.exception.PasswordResetException;
import com.example.ecomerce.exception.PasswordResetLinkException;
import com.example.ecomerce.exception.TokenRefreshException;
import com.example.ecomerce.exception.UserLoginException;
import com.example.ecomerce.exception.UserRegistrationException;
import com.example.ecomerce.mapper.UserMapper;
import com.example.ecomerce.model.CustomUserDetails;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.payload.ApiResponse;
import com.example.ecomerce.model.payload.JwtAuthenticationResponse;
import com.example.ecomerce.model.payload.LoginRequest;
import com.example.ecomerce.model.payload.PasswordResetLinkRequest;
import com.example.ecomerce.model.payload.PasswordResetRequest;
import com.example.ecomerce.model.payload.TokenRefreshRequest;
import com.example.ecomerce.model.token.EmailVerificationToken;
import com.example.ecomerce.model.token.RefreshToken;
import com.example.ecomerce.security.JwtTokenProvider;
import com.example.ecomerce.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Slf4j(topic = "AUTH_CONTROLLER")
@RequestMapping("/auth")
@Api(value = "Authorization Rest API", description = "Defines endpoints that can be hit only when the user is not logged in. It's not secured by default.")

public class AuthController {

  private final UserMapper userMapper;
  private final AuthService authService;
  private final JwtTokenProvider tokenProvider;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthController(UserMapper userMapper,
                        AuthService authService,
                        JwtTokenProvider tokenProvider,
                        ApplicationEventPublisher applicationEventPublisher,
                        AuthenticationManager authenticationManager) {
    this.userMapper = userMapper;
    this.authService = authService;
    this.tokenProvider = tokenProvider;
    this.applicationEventPublisher = applicationEventPublisher;
    this.authenticationManager = authenticationManager;
  }

  /**
   * Checks is a given email is in use or not.
   */
  @ApiOperation(value = "Checks if the given email is in use")
  @GetMapping("/checkEmailInUse")
  public ResponseEntity<ApiResponse<String>> checkEmailInUse(@ApiParam(value = "Email id to "
      + "check against") @RequestParam("email") String email) {
    Boolean emailExists = authService.emailAlreadyExists(email);
    return ResponseEntity.ok(new ApiResponse<>(true, emailExists.toString(),
                                              emailExists.toString()));
  }

  /**
   * Entry point for the user log in. Return the jwt auth token and the refresh token
   */
  @PostMapping("/login")
  @ApiOperation(value = "Logs the user in to the system and return the auth tokens")
  public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@ApiParam(value = "The LoginRequest payload") @Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authService.authenticateUser(loginRequest)
        .orElseThrow(() -> new UserLoginException("error.user.login", "Couldn't login "
            + "user ", loginRequest.getEmail()));
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    log.info("Logged in User returned [API]: " + customUserDetails.getAuthorities().toString());
    log.info("Logged in User returned [API]: " + customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
        .map(RefreshToken::getToken)
        .map(refreshToken -> {
          String jwtToken = authService.generateToken(customUserDetails);
          return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration()));
        })
        .orElseThrow(() -> new UserLoginException("error.user.login","Couldn't create "
            + "refresh token for: ", loginRequest.getEmail()));
  }

  /**
   * Entry point for the user registration process. On successful registration,
   * publish an event to generate email verification token
   */
  //@JsonView(View.UserView.Request.class)
  @PostMapping("/register")
  @ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
  public ResponseEntity<ApiResponse<UserDTO>> registerUser(@ApiParam(value = "The "
      + "RegistrationRequest payload")
  @Valid @RequestBody UserDTO registrationRequest) {
    log.info("USERNAME_X: {}", registrationRequest.getUsername());
    return authService.registerUser(registrationRequest)
        .map(user -> {
          UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
          OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
          applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
          log.info("Registered User returned [API[: " + user);
          UserDTO registrationResponse = userMapper.mapToDto(user);
          return ResponseEntity.ok(new ApiResponse<>(true, registrationResponse,
                                                   "User registered successfully. Check your email for verification"));
        })
        .orElseThrow(() -> new UserRegistrationException("error.user.registration", "Missing user object in database", registrationRequest.getEmail()));
  }

  /**
   * Receives the reset link request and publishes an event to send email id containing
   * the reset link if the request is valid. In future the deeplink should open within
   * the app itself.
   */
  @PostMapping("/password/resetlink")
  @ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
      "reset link")
  public ResponseEntity<ApiResponse<String>> resetLink(@ApiParam(value = "The "
      + "PasswordResetLinkRequest "
+ "payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {

    return authService.generatePasswordResetToken(passwordResetLinkRequest)
        .map(passwordResetToken -> {
          UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
          OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
              urlBuilder);
          applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
          return ResponseEntity.ok(new ApiResponse<>(true, passwordResetToken.getToken(),
                                                   "Password reset link sent successfully"));
        })
        .orElseThrow(() -> new PasswordResetLinkException("error.password.reset.link", "Couldn't create a valid token", passwordResetLinkRequest.getEmail()));
  }

  /**
   * Receives a new passwordResetRequest and sends the acknowledgement after
   * changing the password to the user's mail through the event.
   */

  @PostMapping("/password/reset")
  @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
      "email")
  public ResponseEntity<ApiResponse<String>> resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {

    return authService.resetPassword(passwordResetRequest)
        .map(changedUser -> {
          OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
              "Changed Successfully");
          applicationEventPublisher.publishEvent(onPasswordChangeEvent);
          return ResponseEntity.ok(new ApiResponse<>(true, changedUser.getEmail(),
                                                   "Password changed successfully"));
        })
        .orElseThrow(() -> new PasswordResetException("error.password.reset.link", "Error in resetting password", passwordResetRequest.getToken()));
  }

  /**
   * Confirm the email verification token generated for the user during
   * registration. If token is invalid or token is expired, report error.
   */
  @PostMapping("/registrationConfirmation")
  @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
  public ResponseEntity<ApiResponse<UserDTO>> confirmRegistration(@ApiParam(value = "the "
      + "token that was sent"
+ " to the user email") @RequestBody String token) {
    log.info("TOKEN_CONFIRM:{}", token);
    return authService.confirmEmailRegistration(token)
        .map(user -> {
          UserDTO confirmUserResponse = userMapper.mapToDto(user);
          return ResponseEntity.ok(new ApiResponse<>(true, confirmUserResponse,
                                                     "User verified successfully"));
        })
        .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token, "Failed to confirm. Please generate a new email verification request"));
  }

  /**
   * Resend the email registration mail with an updated token expiry. Safe to
   * assume that the user would always click on the last re-verification email and
   * any attempts at generating new token from past (possibly archived/deleted)
   * tokens should fail and report an exception.
   */
  @GetMapping("/resendRegistrationToken")
  @ApiOperation(value = "Resend the email registration with an updated token expiry. Safe to " +
      "assume that the user would always click on the last re-verification email and " +
      "any attempts at generating new token from past (possibly archived/deleted)" +
      "tokens should fail and report an exception. ")
  public ResponseEntity<ApiResponse<User>> resendRegistrationToken(@ApiParam(value = "the "
      + "initial token that was sent to the user email after registration") @RequestParam("token") String existingToken) {

    EmailVerificationToken newEmailToken = authService.recreateRegistrationToken(existingToken)
        .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "User is already registered. No need to re-generate token"));

    return Optional.ofNullable(newEmailToken.getUser())
        .map(registeredUser -> {
          UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
          OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailToken);
          applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
          return ResponseEntity.ok(new ApiResponse<>(true, registeredUser,
                                                   "Email verification resent successfully"));
        })
        .orElseThrow(() -> new InvalidTokenRequestException("error.refresh.token", "No user associated with this request. Re-verification denied", existingToken));
  }

  /**
   * Refresh the expired jwt token using a refresh token for the specific device
   * and return a new token to the caller
   */
  @PostMapping("/refresh")
  @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
      "updated response tokens")
  public ResponseEntity<JwtAuthenticationResponse> refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest "
+ "payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
    log.info("REFRESH_TOKEN:{}", tokenRefreshRequest.getRefreshToken());
    return authService.refreshJwtToken(tokenRefreshRequest)
        .map(updatedToken -> {
          String refreshToken = tokenRefreshRequest.getRefreshToken();
          log.info("Created new Jwt Auth token: " + updatedToken);
          return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
        })
        .orElseThrow(() -> new TokenRefreshException("error.refresh.token", "Unexpected error during token refresh. Please logout and login again.", tokenRefreshRequest.getRefreshToken()));
  }
}
