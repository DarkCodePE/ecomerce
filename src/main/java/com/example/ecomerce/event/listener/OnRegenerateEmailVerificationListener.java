package com.example.ecomerce.event.listener;

import com.example.ecomerce.event.OnRegenerateEmailVerificationEvent;
import com.example.ecomerce.exception.MailSendException;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.token.EmailVerificationToken;
import com.example.ecomerce.services.MailService;
import freemarker.template.TemplateException;
import java.io.IOException;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Slf4j(topic = "ON_REGENERATE_EMAIL_VERIFICATION_LISTENER")
@Component
public class OnRegenerateEmailVerificationListener implements
    ApplicationListener<OnRegenerateEmailVerificationEvent> {
  private final MailService mailService;

  @Autowired
  public OnRegenerateEmailVerificationListener(MailService mailService) {
    this.mailService = mailService;
  }

  /**
   * As soon as a registration event is complete, invoke the email verification
   */
  @Override
  @Async
  public void onApplicationEvent(OnRegenerateEmailVerificationEvent onRegenerateEmailVerificationEvent) {
    resendEmailVerification(onRegenerateEmailVerificationEvent);
  }

  /**
   * Send email verification to the user and persist the token in the database.
   */
  private void resendEmailVerification(OnRegenerateEmailVerificationEvent event) {
    User user = event.getUser();
    EmailVerificationToken emailVerificationToken = event.getToken();
    String recipientAddress = user.getEmail();

    String emailConfirmationUrl =
        event.getRedirectUrl().queryParam("token", emailVerificationToken.getToken()).toUriString();
    try {
      mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(e.getMessage());
      throw new MailSendException(recipientAddress, "Email Verification");
    }
  }
}
