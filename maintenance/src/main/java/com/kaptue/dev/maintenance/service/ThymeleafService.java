package com.kaptue.dev.maintenance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;

@Service
public class ThymeleafService {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * Traite un template Thymeleaf avec les variables fournies et retourne le HTML en chaîne de caractères.
     * @param templateName Le nom du fichier template (ex: "demande_achat_template.html")
     * @param variables Un map contenant les variables à injecter dans le template.
     * @return Le contenu HTML final.
     */
    public String processHtmlTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}