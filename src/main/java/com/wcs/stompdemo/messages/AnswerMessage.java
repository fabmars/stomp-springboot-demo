package com.wcs.stompdemo.messages;

import lombok.Value;

@Value
public class AnswerMessage {
  private String answer;
  private String playerId;
}
