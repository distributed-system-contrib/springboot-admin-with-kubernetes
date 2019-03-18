package io.github.hamelmoon.listener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessEventListener
    implements ApplicationListener<AuthenticationSuccessEvent> {

  Logger LOGGER = LoggerFactory.getLogger(AuthenticationFailureListener.class);


  public void onApplicationEvent(AuthenticationSuccessEvent e) {

    //TODO : Implementation -> Login Event
    LOGGER.info(e.toString());
  }
}