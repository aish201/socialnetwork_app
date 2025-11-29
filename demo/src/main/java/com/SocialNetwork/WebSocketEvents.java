package com.SocialNetwork;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEvents {
	 @EventListener
	    public void handleSessionConnected(SessionConnectedEvent event) {
	        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
	        System.out.println("WS CONNECTED, sessionId=" + sha.getSessionId() + ", user=" + sha.getUser());
	    }

	    @EventListener
	    public void handleSessionDisconnected(SessionDisconnectEvent event) {
	        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
	        System.out.println("WS DISCONNECTED, sessionId=" + sha.getSessionId() + ", user=" + sha.getUser());
	    }
	}

