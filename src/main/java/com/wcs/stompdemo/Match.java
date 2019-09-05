package com.wcs.stompdemo;

import com.wcs.stompdemo.messages.QuestionMessage;
import lombok.Data;

import java.util.*;

@Data
public class Match {
  private int questionNumber;
  private QuestionMessage currentQuestion;

  private List<String> players = new ArrayList<>(); // player ids
  private Map<String, String> answers = new HashMap<>(); // playerId => answer


  public QuestionMessage getNextQuestion() {
    questionNumber++;
    currentQuestion = new QuestionMessage("Question N°" + questionNumber + " TODO", Arrays.asList("A" + questionNumber, "B" + questionNumber, "C" + questionNumber, "D" + questionNumber));
    return currentQuestion;
  }


  public int countPlayers() {
    return players.size();
  }

  public int countAnswers() {
    return answers.size();
  }

  public boolean isAllAnswered() {
    return countPlayers() == countAnswers();
  }

  public String addPlayer() {
    String playerId = UUID.randomUUID().toString();
    players.add(playerId);
    return playerId;
  }

  public void addAnswer(String playedId, String answer) {
    if(players.contains(playedId)) {
      answers.put(playedId, answer); // like this a player can even change his answer before the last player has answered
    } else {
      throw new IllegalArgumentException("Unknown player: " + playedId);
    }
  }

  public void removePlayer(String playerId) {
    players.remove(playerId);
    answers.remove(playerId);
  }

  public void clearAnswers() {
    answers.clear();
  }
}
