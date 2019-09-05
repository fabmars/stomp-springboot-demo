package com.wcs.stompdemo.messages;

import lombok.Value;

@Value
public class PlayerJoinedMessage {
  private String playerId;
  private QuestionMessage currentQuestion;
}
