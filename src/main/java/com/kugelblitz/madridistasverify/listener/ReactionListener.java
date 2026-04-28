package com.kugelblitz.madridistasverify.listener;

import com.kugelblitz.madridistasverify.service.VerificationService;
import java.util.Objects;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReactionListener extends ListenerAdapter {

  private final VerificationService verificationService;

  @Autowired
  public ReactionListener(VerificationService verificationService) {
    this.verificationService = verificationService;
  }

  @Override
  public void onMessageReactionAdd(MessageReactionAddEvent event) {
    if (Objects.requireNonNull(event.getUser()).isBot()) return;

    if (event.getMessageId().equals("1497965546476408995")) {
      System.out.println("Received a reaction to the message!");
      verificationService.startVerification(event.getUser(), event.getGuild());
    }
  }
}
