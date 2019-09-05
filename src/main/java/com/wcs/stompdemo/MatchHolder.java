package com.wcs.stompdemo;

import lombok.Getter;
import org.springframework.stereotype.Component;

// The goal is to have ONE instance of Match in this particular app
// Don't do this at home
@Component
@Getter
public class MatchHolder {
  private Match match;

  public MatchHolder() {
    match = new Match();
  }
}
