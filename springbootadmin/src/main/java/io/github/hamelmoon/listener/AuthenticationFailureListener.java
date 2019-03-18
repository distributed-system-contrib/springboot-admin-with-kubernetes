package io.github.hamelmoon.listener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFailureListener
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {


  Logger LOGGER = LoggerFactory.getLogger(AuthenticationFailureListener.class);


  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {

    //TODO : Implementation -> Login Event
    LOGGER.info(e.toString());
  }
}
