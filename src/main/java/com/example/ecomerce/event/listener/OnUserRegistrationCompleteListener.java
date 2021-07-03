package com.example.ecomerce.event.listener;

import com.example.ecomerce.event.OnUserRegistrationCompleteEvent;
import com.example.ecomerce.exception.MailSendException;
import com.example.ecomerce.model.User;
import com.example.ecomerce.services.EmailVerificationTokenService;
import com.example.ecomerce.services.MailService;
import freemarker.template.TemplateException;
import java.io.IOException;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j(topic = "ON_USER_REGISTRATION_COMPLETE_LISTENER")
@Component
public class OnUserRegistrationCompleteListener implements
    ApplicationListener<OnUserRegistrationCompleteEvent> {
  private final EmailVerificationTokenService emailVerificationTokenService;
  private final MailService mailService;

  @Autowired
  public OnUserRegistrationCompleteListener(EmailVerificationTokenService emailVerificationTokenService, MailService mailService) {
    this.emailVerificationTokenService = emailVerificationTokenService;
    this.mailService = mailService;
  }

  /**
   * As soon as a registration event is complete, invoke the email verification
   * asynchronously in an another thread pool
   */
  @Override
  @Async
  public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {
    sendEmailVerification(onUserRegistrationCompleteEvent);
  }

  /**
   * Send email verification to the user and persist the token in the database.
   */
  private void sendEmailVerification(OnUserRegistrationCompleteEvent event) {
    User user = event.getUser();
    String token = emailVerificationTokenService.generateNewToken();
    emailVerificationTokenService.createVerificationToken(user, token);

    String recipientAddress = user.getEmail();
    /*String emailConfirmationUrl =
        event.getRedirectUrl().queryParam("token", token).toUriString();*/

    try {
      mailService.sendEmailVerification(token, recipientAddress);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(e.getMessage());
      throw new MailSendException(recipientAddress, "Email Verification");
    }
  }
}
