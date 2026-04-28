package com.kugelblitz.madridistasverify.domain;

import lombok.Getter;

@Getter
public class VerificationSession {

  private int questionIndex = 0;

  public void incrementQuestionIndex() {
    this.questionIndex++;
  }
}
