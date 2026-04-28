package com.kugelblitz.madridistasverify.listener;

import com.kugelblitz.madridistasverify.service.VerificationService;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends ListenerAdapter {
  private final VerificationService verificationService;

  @Autowired
  public MessageListener(VerificationService verificationService) {
    this.verificationService = verificationService;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    if (event.isFromType(ChannelType.PRIVATE)) {
      verificationService.saveAnswer(event.getAuthor().getId(), event.getMessage().getContentRaw());
    }
  }
}
