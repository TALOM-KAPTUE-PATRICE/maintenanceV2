package com.kaptue.dev.maintenance.service;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service; // Pour envoyer l'email en arrière-plan
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine; 

    @Value("${cors.allowed-origins}") // URL de votre frontend Angular
    private String frontendUrl;

    /**
     * Envoie un email de réinitialisation de mot de passe en utilisant un template HTML.
     * @param to L'adresse email du destinataire.
     * @param token Le token de réinitialisation à usage unique.
     */
    @Async // Permet à l'envoi de l'email de ne pas bloquer la réponse HTTP
    public void sendPasswordResetEmail(String to, String userName, String token) {
        String subject = "Réinitialisation de votre mot de passe pour Maint-Pro";
        String resetUrl = frontendUrl + "/auth/reset-password?token=" + token;

        // Préparer les variables pour le template Thymeleaf
        Map<String, Object> templateModel = Map.of(
            "recipientName", userName,
            "resetUrl", resetUrl,
            "appName", "Maint-Pro"
        );

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("email/password-reset-template.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indique que le contenu est du HTML
            mailSender.send(message);
            logger.info("Email HTML envoyé avec succès à {}", to);
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email HTML à {}: {}", to, e.getMessage(), e);
            // Gérer l'exception, par exemple en la relançant comme une exception personnalisée
            // throw new EmailSendingException("Erreur lors de l'envoi de l'email", e);
        }
    }
}