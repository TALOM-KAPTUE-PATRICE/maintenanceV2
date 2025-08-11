package com.kaptue.dev.maintenance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        // ▼▼▼ INJECTEZ LA PROPRIÉTÉ ▼▼▼
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // L'endpoint que le client Angular utilisera pour se connecter.
        // setAllowedOrigins est crucial pour autoriser la connexion depuis votre client Angular.
        registry.addEndpoint("/ws")
                .setAllowedOrigins(allowedOrigins.split(",")) // URL de votre front-end
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Préfixe pour les messages envoyés depuis le client vers le serveur.
        // Ex: Le client enverra à "/app/hello".
        config.setApplicationDestinationPrefixes("/app");

        // Préfixe pour les "topics" (sujets) auxquels les clients peuvent s'abonner.
        // Le serveur enverra des messages à des destinations comme "/topic/notifications" ou "/user/queue/private".
        // On active un simple broker en mémoire.
        // "/topic" est pour les messages publics (broadcast).
        // "/queue" est souvent utilisé pour les messages privés point-à-point.
        config.enableSimpleBroker("/topic", "/queue");
        
        // Active l'envoi de messages à un utilisateur spécifique via des destinations comme "/user/queue/specific-user".
        config.setUserDestinationPrefix("/user");
    }
}