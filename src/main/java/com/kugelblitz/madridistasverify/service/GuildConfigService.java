package com.kugelblitz.madridistasverify.service;

import com.kugelblitz.madridistasverify.domain.entity.GuildConfig;
import com.kugelblitz.madridistasverify.repository.GuildConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildConfigService {

  private static final int DEFAULT_ACCOUNT_AGE = 30;
  private final GuildConfigRepository repository;

  @Autowired
  public GuildConfigService(GuildConfigRepository repository) {
    this.repository = repository;
  }

  public int getMinAccountAge(String guildId) {
    return repository
        .findById(guildId)
        .map(GuildConfig::getMinAccountAgeDays)
        .orElse(DEFAULT_ACCOUNT_AGE);
  }

  public void setMinAccountAge(String guildId, int days) {
    GuildConfig config = repository.findById(guildId).orElse(new GuildConfig());
    config.setGuildId(guildId);
    config.setMinAccountAgeDays(days);
    repository.save(config);
  }
}
