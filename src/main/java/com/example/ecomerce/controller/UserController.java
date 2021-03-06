package com.example.ecomerce.controller;

import com.example.ecomerce.annotation.CurrentUser;
import com.example.ecomerce.event.OnUserAccountChangeEvent;
import com.example.ecomerce.event.OnUserLogoutSuccessEvent;
import com.example.ecomerce.exception.UpdatePasswordException;
import com.example.ecomerce.model.CustomUserDetails;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.payload.ApiResponse;
import com.example.ecomerce.model.payload.LogOutRequest;
import com.example.ecomerce.model.payload.UpdatePasswordRequest;
import com.example.ecomerce.services.AuthService;
import com.example.ecomerce.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Slf4j(topic = "USER_CONTROLLER")
@RequestMapping("/user")
@Api(value = "User Rest API", description = "Defines endpoints for the logged in user. It's secured by default")
public class UserController {

  private final AuthService authService;
  private final UserService userService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public UserController(AuthService authService,
      UserService userService,
      ApplicationEventPublisher applicationEventPublisher) {
    this.authService = authService;
    this.userService = userService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  /**
   * Gets the current user profile of the logged in user
   */
  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Returns the current user profile")
  public ResponseEntity getUserProfile(@CurrentUser CustomUserDetails currentUser) {
    log.info(currentUser.getEmail() + " has role: " + currentUser.getRoles());
    return ResponseEntity.ok("Hello. This is about me");
  }

  /**
   * Returns all admins in the system. Requires Admin access
   */
  @GetMapping("/admins")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiOperation(value = "Returns the list of configured admins. Requires ADMIN Access")
  public ResponseEntity getAllAdmins() {
    log.info("Inside secured resource with admin");
    return ResponseEntity.ok("Hello. This is about admins");
  }

  /**
   * Updates the password of the current logged in user
   */
  @PostMapping("/password/update")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Allows the user to change his password once logged in by supplying the correct current " +
      "password")
  public ResponseEntity<ApiResponse<User>> updateUserPassword(@CurrentUser CustomUserDetails customUserDetails,
      @ApiParam(value = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

    return authService.updatePassword(customUserDetails, updatePasswordRequest)
        .map(updatedUser -> {
          OnUserAccountChangeEvent onUserPasswordChangeEvent = new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
          applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);
          return ResponseEntity.ok(new ApiResponse<>(true, updatedUser,
                                                     "Password changed successfully"));
        })
        .orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));
  }

  /**
   * Log the user out from the app/device. Release the refresh token associated with the
   * user device.
   */
  @PostMapping("/logout")
  @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
  public ResponseEntity<ApiResponse<Object>> logoutUser(@CurrentUser CustomUserDetails customUserDetails,
      @ApiParam(value = "The LogOutRequest payload") @Valid @RequestBody LogOutRequest logOutRequest) {
    userService.logoutUser(customUserDetails, logOutRequest);
    Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

    OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(), credentials.toString(), logOutRequest);
    applicationEventPublisher.publishEvent(logoutSuccessEvent);
    return ResponseEntity.ok(new ApiResponse<>(true, null, "Log out successful"));
  }
}
