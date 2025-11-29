package com.SocialNetwork;  // Or move to com.SocialNetwork.config for better organization
//
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import com.SocialNetwork.security.JwtUtil;  // Adjust if path differs
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//import java.util.Map;
//import java.util.logging.Logger;  // Or use SLF4J: import org.slf4j.LoggerFactory;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class AuthHandshakeInterceptor implements HandshakeInterceptor {
//
//    private static final Logger logger = Logger.getLogger(AuthHandshakeInterceptor.class.getName());  // Simple logging
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//
//        // Extract token from query params (e.g., ws?access_token=...)
//        String query = request.getURI().getQuery();
//        String token = null;
//        if (query != null && query.contains("access_token=")) {
//            // More robust: Split on '&' or use URI parsing
//            String[] params = query.split("&");
//            for (String param : params) {
//                if (param.startsWith("access_token=")) {
//                    token = param.substring(13);
//                    break;
//                }
//            }
//        }
//
//        if (token != null && token.startsWith("Bearer ")) {
//            try {
//                token = token.substring(7);
//                String email = jwtUtil.extractUsername(token);  // Assumes this returns email/username
//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                
//                // Create auth token (use userDetails.getUsername() if principal is username, not email)
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(auth);
//                
//                // Store username for session/topic resolution (match your subscription logic)
//                attributes.put("userEmail", email);
//                attributes.put("username", userDetails.getUsername());  // Add this for consistency
//                
//                logger.info("✅ WebSocket handshake authenticated for user: " + userDetails.getUsername());
//                return true;
//            } catch (Exception e) {  // Catch JWT exceptions (e.g., ExpiredJwtException, MalformedJwtException)
//                logger.warning("❌ WebSocket auth failed: " + e.getMessage());
//                return false;
//            }
//        }
//        
//        logger.warning("❌ WebSocket handshake rejected: No valid token");
//        return false;  // Reject unauth
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception exception) {
//        // Optional: Cleanup or logging here if needed
//        if (exception != null) {
//            logger.severe("WebSocket handshake failed: " + exception.getMessage());
//        }
//    }
//}
//package com.SocialNetwork;
//
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import com.SocialNetwork.security.JwtUtil;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//import java.util.Map;
//import java.util.logging.Logger;
//
//public class AuthHandshakeInterceptor implements HandshakeInterceptor {
//
//    private static final Logger logger = Logger.getLogger(AuthHandshakeInterceptor.class.getName());
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//	@Override
//	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//			Map<String, Object> attributes) throws Exception {
//		// TODO Auto-generated method stub
//		String token = null;
//
//        // ✅ Check query param
//        String query = request.getURI().getQuery();
//        if (query != null) {
//            for (String param : query.split("&")) {
//                if (param.startsWith("access_token=")) {
//                    token = param.substring("access_token=".length());
//                    break;
//                }
//            }
//        }
//
//        // ✅ Check Authorization header
//        if (token == null && request.getHeaders().containsKey("Authorization")) {
//            String authHeader = request.getHeaders().getFirst("Authorization");
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                token = authHeader.substring(7);
//            }
//        }
//
//        if (token != null && !token.isEmpty()) {
//            try {
//                String email = jwtUtil.extractUsername(token);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//                UsernamePasswordAuthenticationToken auth =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(auth);
//                attributes.put("username", userDetails.getUsername());
//
//                logger.info("✅ WebSocket handshake authenticated: " + userDetails.getUsername());
//                return true;
//
//            } catch (Exception e) {
//                logger.warning("❌ WebSocket auth failed: " + e.getMessage());
//                response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//                return false;
//            }
//        }
//
//        logger.warning("❌ WebSocket handshake rejected: No token");
//        response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
//		return false;
//	}
//
//	@Override
//	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//			Exception exception) {
//		// TODO Auto-generated method stub
//		
//	}
//}
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		// TODO Auto-generated method stub
//	    // Cast ServerHttpRequest to ServletServerHttpRequest to access query parameters
//        if (request instanceof ServletServerHttpRequest servletRequest) {
//            String token = servletRequest.getServletRequest().getParameter("access_token");
//
//            if (token != null && !token.isEmpty()) {
//                // Store token in attributes for later use in JwtChannelInterceptor
//                attributes.put("access_token", token);
//            } else {
//                System.out.println("⚠️ No access_token found in WebSocket handshake query params");
//            }
//        }
//
//        return true; // allow handshake to proceed
//    }
	    if (request.getURI().getQuery() != null) {
            String[] params = request.getURI().getQuery().split("&");
            for (String param : params) {
                String[] keyVal = param.split("=");
                if ("access_token".equals(keyVal[0]) && keyVal.length == 2) {
                    attributes.put("access_token", keyVal[1]);
                }
            }
        }
        return true;
    }

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}
}


