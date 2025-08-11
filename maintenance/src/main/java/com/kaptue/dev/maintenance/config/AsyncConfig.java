package com.kaptue.dev.maintenance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration personnalisée pour l'exécution des tâches asynchrones (@Async).
 * Définit un TaskExecutor par défaut pour éviter l'ambiguïté lorsque plusieurs
 * exécuteurs existent dans le contexte (notamment avec WebSocket).
 */
@Configuration
public class AsyncConfig {

    /**
     * Définit le bean TaskExecutor principal qui sera utilisé par défaut
     * pour toutes les méthodes annotées avec @Async.
     * @return un ThreadPoolTaskExecutor configuré.
     */
    @Bean(name = "taskExecutor") // Le nommer "taskExecutor" résout l'avertissement de Spring.
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // Nombre de threads à garder dans le pool
        executor.setMaxPoolSize(10);   // Nombre maximum de threads
        executor.setQueueCapacity(25); // Taille de la file d'attente pour les tâches en attente
        executor.setThreadNamePrefix("Async-"); // Préfixe pour les noms de threads pour le logging
        executor.initialize();
        return executor;
    }
}