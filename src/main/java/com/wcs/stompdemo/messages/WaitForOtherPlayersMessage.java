package com.wcs.stompdemo.messages;

import lombok.Data;
import lombok.Getter;

@Getter
public class WaitForOtherPlayersMessage implements AnswerAckMessage {

  private boolean pleaseWait = true; // always true, will be used to know which type the message is
  private int answerCount;

  public WaitForOtherPlayersMessage(int answerCount) {
    this.answerCount = answerCount;
  }
}
