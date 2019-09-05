package com.wcs.stompdemo;

import com.wcs.stompdemo.messages.PlayerCountMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketEventListener {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @Autowired
  private MatchHolder matchHolder;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    log.info("Received a new web socket connection: " + sessionId);
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String playerId = (String) headerAccessor.getSessionAttributes().get("playerId");
    log.info("User Disconnected : " + playerId);

    Match match = matchHolder.getMatch();
    match.removePlayer(playerId);

    PlayerCountMessage message = new PlayerCountMessage(match.countPlayers());
    messagingTemplate.convertAndSend(QAController.TOPIC_STATUS, message);
  }
}