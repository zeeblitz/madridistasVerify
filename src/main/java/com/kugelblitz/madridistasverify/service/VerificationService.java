package com.kugelblitz.madridistasverify.service;

import com.kugelblitz.madridistasverify.domain.VerificationSession;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

  private final Map<String, List<String>> userAnswers = new HashMap<>();
  private final ScoringService scoringService;
  private final GuildConfigService guildConfigService;
  private final JDA jda;
  private final Map<String, VerificationSession> activeSessions = new HashMap<>();

  private final List<String> questions =
      List.of(
          "Who/what inspired you to be a Madridista?",
          "Do you support any other club?",
          "What is your favorite aspect of the club?",
          "Thoughts on Real Madrid Femenino?");

  public VerificationService(
      ScoringService scoringService, GuildConfigService guildConfigService, JDA jda) {
    this.scoringService = scoringService;
    this.guildConfigService = guildConfigService;
    this.jda = jda;
  }

  public void startVerification(User user, Guild guild) {

    Member member = guild.getMember(user);
    String userId = user.getId();

    // 1. Check Duplicate Session
    if (activeSessions.containsKey(userId)) {
      user.openPrivateChannel()
          .queue(
              privateChannel ->
                  privateChannel.sendMessage("You already have an ongoing verification.").queue());
      return;
    }

    // 2. Check account age (> 30 days)
    assert member != null;
    int minDays = guildConfigService.getMinAccountAge(guild.getId());
    if (member.getTimeCreated().isAfter(OffsetDateTime.now().minusDays(minDays))) {
      user.openPrivateChannel()
          .queue(
              channel ->
                  channel
                      .sendMessage(
                          "Your account must be at least 30 days old to apply for verification")
                      .queue());
      return;
    }

    // 3. Check if the user already has verified role
    boolean alreadyVerified =
        member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("Madridista"));
    if (alreadyVerified) {
      user.openPrivateChannel()
          .queue(channel -> channel.sendMessage("You are already verified").queue());
      return;
    }

    // 4. Create session
    activeSessions.put(userId, new VerificationSession());

    // 5. Send first question
    sendNextQuestions(user);
  }

  private void sendNextQuestions(User user) {
    VerificationSession session = activeSessions.get(user.getId());
    int index = session.getQuestionIndex();
    if (index >= questions.size()) return;
    String question = questions.get(index);
    user.openPrivateChannel()
        .queue(channel -> channel.sendMessage("Question " + (index + 1) + "\n" + question).queue());
  }

  public void saveAnswer(String id, String contentRaw) {
    System.out.println("Received the message " + id + " with content " + contentRaw);
    userAnswers.computeIfAbsent(id, k -> new ArrayList<>()).add(contentRaw);
  }

  public void evaluate(String userId, User user) {
    List<String> answers = userAnswers.get(userId);
    int score = scoringService.calculateScore(answers);

    if (score >= 20) {
      approveUser(user);
    } else {
      rejectUser(user);
    }
  }

  private void rejectUser(User user) {
    // Send rejection message
  }

  private void approveUser(User user) {
    Guild guild = jda.getGuildById("<GUILD_ID>");
    assert guild != null;
    Member member = guild.retrieveMember(user).complete();

    Role role = guild.getRolesByName("Madridista", true).get(0);
    guild.addRoleToMember(member, role);
  }
}
