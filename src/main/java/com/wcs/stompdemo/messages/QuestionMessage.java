package com.wcs.stompdemo.messages;

import lombok.Value;

import java.util.List;

@Value
public class QuestionMessage implements AnswerAckMessage {

  private String question;
  private List<String> answers;
}
