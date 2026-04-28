package com.kugelblitz.madridistasverify.listener;

import com.kugelblitz.madridistasverify.service.GuildConfigService;
import com.kugelblitz.madridistasverify.service.VerificationService;
import java.util.*;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlashCommandListener extends ListenerAdapter {

  private static final Set<String> allowedCommands = new HashSet<>(Set.of("verify", "setminage"));
  private final VerificationService verificationService;
  private final GuildConfigService guildConfigService;

  @Autowired
  public SlashCommandListener(
      VerificationService verificationService, GuildConfigService guildConfigService) {
    this.verificationService = verificationService;
    this.guildConfigService = guildConfigService;
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    if (!allowedCommands.contains(event.getName())) return;
    User user = event.getUser();

    if (event.getName().equals("verify")) {
      event.reply("Starting your verification...check your DMs.").setEphemeral(true).queue();
      verificationService.startVerification(user, Objects.requireNonNull(event.getGuild()));
    }

    if (event.getName().equals("setMinAge")) {
      int days = Objects.requireNonNull(event.getOption("days")).getAsInt();
      guildConfigService.setMinAccountAge(Objects.requireNonNull(event.getGuild()).getId(), days);
      event.reply("Minimum account age set to " + days + " days.").setEphemeral(true).queue();
    }
  }
}
