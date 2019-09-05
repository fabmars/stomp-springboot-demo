package com.wcs.stompdemo;

import com.wcs.stompdemo.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class QAController {

  public static final String TOPIC_JOIN = "/topic/join";
  public static final String TOPIC_STATUS = "/topic/status";
  public static final String TOPIC_QUESTION = "/topic/question";

  @Autowired
  private MatchHolder matchHolder;
  @Autowired
  SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/join")
  @SendToUser(TOPIC_JOIN)
  public PlayerJoinedMessage join(StompHeaderAccessor headerAccessor) {
    Match match = matchHolder.getMatch();

    String playerId = match.addPlayer();
    headerAccessor.getSessionAttributes().put("playerId", playerId);
    // this is broadcast
    PlayerCountMessage countMessage = new PlayerCountMessage(match.countPlayers());
    messagingTemplate.convertAndSend(QAController.TOPIC_STATUS, countMessage);

    // this is send to the sender only
    // currentQuestion will be null if the game isn't started
    return new PlayerJoinedMessage(playerId, match.getCurrentQuestion());
  }

  // FIXME caution, only the GM should be able to message here !
  @MessageMapping("/start")
  @SendTo(TOPIC_QUESTION)
  public QuestionMessage start() {
    return matchHolder.getMatch().getNextQuestion();
  }

  @MessageMapping("/answer")
  @SendTo(TOPIC_QUESTION)
  public AnswerAckMessage answer(AnswerMessage message) {
    Match match = matchHolder.getMatch();
    match.addAnswer(message.getPlayerId(), message.getAnswer());

    if(match.isAllAnswered()) {
      match.clearAnswers();
      return matchHolder.getMatch().getNextQuestion();
    } else {
      return new WaitForOtherPlayersMessage(match.countAnswers()); // great, so the client can even count how many players responded
    }
  }
}