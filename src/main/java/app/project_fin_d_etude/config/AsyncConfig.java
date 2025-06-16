package app.project_fin_d_etude.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.Executor;

/**
 * Configuration de l'exécution asynchrone dans l'application. Cette classe
 * configure le pool de threads pour gérer les opérations asynchrones.
 */
@Configuration
@EnableAsync // Active le support asynchrone dans Spring
public class AsyncConfig {

    /**
     * Configure et crée un pool de threads pour l'exécution des tâches
     * asynchrones.
     *
     * @return Un Executor configuré pour gérer les tâches asynchrones
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Nombre de threads qui seront toujours actifs
        executor.setCorePoolSize(2);

        // Nombre maximum de threads que le pool peut créer
        executor.setMaxPoolSize(4);

        // Nombre maximum de tâches en attente dans la file
        executor.setQueueCapacity(100);

        // Préfixe pour identifier les threads dans les logs
        executor.setThreadNamePrefix("AsyncThread-");

        // Initialise le pool de threads
        executor.initialize();

        return executor;
    }
}
