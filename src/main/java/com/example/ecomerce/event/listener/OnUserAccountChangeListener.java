package com.example.ecomerce.event.listener;

import com.example.ecomerce.event.OnUserAccountChangeEvent;
import com.example.ecomerce.exception.MailSendException;
import com.example.ecomerce.model.User;
import com.example.ecomerce.services.MailService;
import freemarker.template.TemplateException;
import java.io.IOException;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j(topic = "ON_USER_ACCOUNT_CHANGE_LISTENER")
@Component
public class OnUserAccountChangeListener implements ApplicationListener<OnUserAccountChangeEvent> {
  private final MailService mailService;

  @Autowired
  public OnUserAccountChangeListener(MailService mailService) {
    this.mailService = mailService;
  }

  /**
   * As soon as a registration event is complete, invoke the email verification
   * asynchronously in an another thread pool
   */
  @Override
  @Async
  public void onApplicationEvent(OnUserAccountChangeEvent onUserAccountChangeEvent) {
    sendAccountChangeEmail(onUserAccountChangeEvent);
  }

  /**
   * Send email verification to the user and persist the token in the database.
   */
  private void sendAccountChangeEmail(OnUserAccountChangeEvent event) {
    User user = event.getUser();
    String action = event.getAction();
    String actionStatus = event.getActionStatus();
    String recipientAddress = user.getEmail();

    try {
      mailService.sendAccountChangeEmail(action, actionStatus, recipientAddress);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(e.getMessage());
      throw new MailSendException("error.mail.send", "Account Change Mail", recipientAddress);
    }
  }
}
