
//package com.SocialNetwork.security;
//
//import com.SocialNetwork.repository.UserRepository;
//import com.SocialNetwork.security.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class JwtChannelInterceptor implements ChannelInterceptor {
//
//    @Autowired private JwtUtil jwtUtil;
//    @Autowired private UserRepository userRepository;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            String token = accessor.getFirstNativeHeader("Authorization");
//            if (token != null && token.startsWith("Bearer ")) {
//                token = token.substring(7);
//                String email = jwtUtil.extractUsername(token);
//
//                userRepository.findByEmail(email).ifPresent(user -> {
//                    UsernamePasswordAuthenticationToken auth =
//                        new UsernamePasswordAuthenticationToken(user.getUsername(), null, List.of());
//                    accessor.setUser(auth);  // ✅ set Principal
//                    System.out.println("STOMP CONNECT: user=" + user.getUsername() + ", session=" + accessor.getSessionId());
//                });
//            }
//        }
//
//        return message;
//    }
//}
//package com.SocialNetwork.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtChannelInterceptor implements ChannelInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
//            // 1️⃣ Read token from STOMP headers
//            String authHeader = accessor.getFirstNativeHeader("Authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("access_token");
//
//            // 2️⃣ Fallback: read token from handshake session attributes
//            if ((authHeader == null || authHeader.isEmpty()) && accessor.getSessionAttributes() != null) {
//                Object tokenAttr = accessor.getSessionAttributes().get("access_token");
//                if (tokenAttr != null) authHeader = tokenAttr.toString();
//            }
//
//            if (authHeader == null || authHeader.isEmpty()) {
//                System.out.println("❌ Missing Authorization token in STOMP CONNECT");
//                return message;
//            }
//
//            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//
//            try {
//                String username = jwtUtil.extractUsername(token);
//                if (username == null || !jwtUtil.validateToken(token)) {
//                    throw new IllegalArgumentException("Invalid JWT token");
//                }
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
//                accessor.setUser(authentication);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                System.out.println("✅ WebSocket CONNECT authenticated for user: " + username);
//
//            } catch (Exception ex) {
//                System.out.println("❌ WebSocket CONNECT rejected: " + ex.getMessage());
//            }
//        }
//
//        return message;
//    }
//}
//package com.SocialNetwork.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtChannelInterceptor implements ChannelInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
//            // 1️⃣ Read token from STOMP headers
//            String authHeader = accessor.getFirstNativeHeader("Authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("access_token");
//
//            // 2️⃣ Fallback: read token from handshake session attributes
//            if ((authHeader == null || authHeader.isEmpty()) && accessor.getSessionAttributes() != null) {
//                Object tokenAttr = accessor.getSessionAttributes().get("access_token");
//                if (tokenAttr != null) authHeader = tokenAttr.toString();
//            }
//
//            if (authHeader == null || authHeader.isEmpty()) {
//                System.out.println("❌ Missing Authorization token in STOMP CONNECT");
//                return message;
//            }
//
//            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//
//            try {
//                // 3️⃣ Extract username from JWT
//                String username = jwtUtil.extractUsername(token);
//                if (username == null || !jwtUtil.validateToken(token)) {
//                    throw new IllegalArgumentException("Invalid JWT token");
//                }
//
//                // 4️⃣ Load full user details (optional, for roles if needed)
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                // 5️⃣ Set the STOMP principal to the exact username
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails.getUsername(),  // ✅ This username will be used in convertAndSendToUser()
//                                null,
//                                userDetails.getAuthorities()
//                        );
//                accessor.setUser(authentication);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                System.out.println("✅ WebSocket CONNECT authenticated for user: " + username);
//
//            } catch (Exception ex) {
//                System.out.println("❌ WebSocket CONNECT rejected: " + ex.getMessage());
//            }
//        }
//
//        return message;
//    }
//}
//package com.SocialNetwork.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.security.Principal;
//
//@Component
//public class JwtChannelInterceptor implements ChannelInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
//            String authHeader = accessor.getFirstNativeHeader("Authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("access_token");
//
//            if ((authHeader == null || authHeader.isEmpty()) && accessor.getSessionAttributes() != null) {
//                Object tokenAttr = accessor.getSessionAttributes().get("access_token");
//                if (tokenAttr != null) authHeader = tokenAttr.toString();
//            }
//
//            if (authHeader == null || authHeader.isEmpty()) {
//                System.out.println("❌ Missing Authorization token in STOMP CONNECT");
//                return message;
//            }
//
//            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//
//            try {
//                String email = jwtUtil.extractUsername(token); // your email
//                if (email == null || !jwtUtil.validateToken(token)) {
//                    throw new IllegalArgumentException("Invalid JWT token");
//                }
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//                // ✅ Set a Principal with the exact username you want for STOMP destination
//                String stompUsername = userDetails.getUsername(); // e.g., "aiswaryachittazhy@gmail.com"
//                Principal principal = new Principal() {
//                    @Override
//                    public String getName() {
//                        return stompUsername;
//                    }
//                };
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(principal, null, userDetails.getAuthorities());
//
//                accessor.setUser(authentication);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                System.out.println("✅ WebSocket CONNECT authenticated for user: " + stompUsername);
//
//            } catch (Exception ex) {
//                System.out.println("❌ WebSocket CONNECT rejected: " + ex.getMessage());
//            }
//        }
//
//        return message;
//    }
//}
//package com.SocialNetwork.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.security.Principal;
//
//@Component
//public class JwtChannelInterceptor implements ChannelInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
//            // Get JWT token from headers
//            String authHeader = accessor.getFirstNativeHeader("Authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("authorization");
//            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("access_token");
//
//            // Fallback: session attributes
//            if ((authHeader == null || authHeader.isEmpty()) && accessor.getSessionAttributes() != null) {
//                Object tokenAttr = accessor.getSessionAttributes().get("access_token");
//                if (tokenAttr != null) authHeader = tokenAttr.toString();
//            }
//
//            if (authHeader == null || authHeader.isEmpty()) {
//                System.out.println("❌ Missing Authorization token in STOMP CONNECT");
//                return message;
//            }
//
//            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
//
//            try {
//                // Validate JWT
//                String email = jwtUtil.extractUsername(token);
//                if (email == null || !jwtUtil.validateToken(token)) {
//                    throw new IllegalArgumentException("Invalid JWT token");
//                }
//
//                // Load user
//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//                // ✅ Set Principal directly for STOMP (used by convertAndSendToUser)
//                Principal principal = () -> userDetails.getUsername(); // exact username/email
//                accessor.setUser(principal);
//
//                // Optional: set SecurityContext for Spring Security
//                SecurityContextHolder.getContext().setAuthentication(
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
//                );
//
//                System.out.println("✅ WebSocket CONNECT authenticated for user: " + userDetails.getUsername());
//
//            } catch (Exception ex) {
//                System.out.println("❌ WebSocket CONNECT rejected: " + ex.getMessage());
//            }
//        }
//
//        return message;
//    }
//}

package com.SocialNetwork.security;

import com.SocialNetwork.security.StompPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("authorization");
            if (authHeader == null) authHeader = accessor.getFirstNativeHeader("access_token");

            if ((authHeader == null || authHeader.isEmpty()) && accessor.getSessionAttributes() != null) {
                Object tokenAttr = accessor.getSessionAttributes().get("access_token");
                if (tokenAttr != null) authHeader = tokenAttr.toString();
            }

            if (authHeader == null || authHeader.isEmpty()) {
                System.out.println("❌ Missing Authorization token in STOMP CONNECT");
                return message;
            }

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            try {
                String email = jwtUtil.extractUsername(token);
                if (email == null || !jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("Invalid JWT token");
                }

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // ✅ Use StompPrincipal for user recognition in Spring
                StompPrincipal principal = new StompPrincipal(userDetails.getUsername());
                accessor.setUser(principal);

                System.out.println("✅ WebSocket CONNECT authenticated for user: " + principal.getName());

            } catch (Exception ex) {
                System.out.println("❌ WebSocket CONNECT rejected: " + ex.getMessage());
            }
        }

        return message;
    }
}





