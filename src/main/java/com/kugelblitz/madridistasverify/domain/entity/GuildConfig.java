package com.kugelblitz.madridistasverify.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class GuildConfig {
  @Id private String guildId;
  private int minAccountAgeDays;
}
