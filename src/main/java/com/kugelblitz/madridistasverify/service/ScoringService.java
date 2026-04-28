package com.kugelblitz.madridistasverify.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScoringService {

  public int calculateScore(List<String> answers) {
    int score = 0;
    for (String ans : answers) {
      if (ans.split("\\s+").length > 10) {
        score += 10;
      } else {
        score -= 5;
      }
    }
    return score;
  }
}
