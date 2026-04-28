package com.kugelblitz.madridistasverify.config;

import com.kugelblitz.madridistasverify.listener.MessageListener;
import com.kugelblitz.madridistasverify.listener.ReactionListener;
import com.kugelblitz.madridistasverify.listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

  @Bean
  public CommandLineRunner init(
      JDA jda,
      ReactionListener reactionListener,
      SlashCommandListener slashCommandListener,
      MessageListener messageListener) {
    System.out.println("Initializing the listeners");
    return args -> jda.addEventListener(reactionListener, slashCommandListener, messageListener);
  }

  @Bean
  public CommandLineRunner registerCommands(JDA jda) {
    return args ->
        jda.updateCommands()
            .addCommands(
                Commands.slash("verify", "Start verification process"),
                Commands.slash("setminage", "Set minimum Account age required for verification")
                    .addOption(OptionType.INTEGER, "days", "Minimum account age in days", true))
            .queue();
  }
}
