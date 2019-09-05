package com.wcs.stompdemo.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class PlayerCountMessage {
  private int playerCount;
}
