package com.gds.challenge26;

import com.gds.challenge26.service.SessionManager;
import com.gds.challenge26.service.SessionService;
import com.gds.challenge26.util.CommandLineInput;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


/**
 * Entry point for the Challenge 26 Spring Boot application.
 * <p>
 * This class bootstraps the Spring context, enables Spring Batch processing,
 * and initializes the command-line driven session workflow.
 */
@SpringBootApplication
@EnableBatchProcessing
public class Challenge26Application {

    /**
     * Main method used to launch the Spring Boot application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {

        SpringApplication.run(Challenge26Application.class, args);

    }
    /**
     * Defines a {@link CommandLineRunner} bean that starts the session workflow
     * after the Spring application context is fully initialized.
     * <p>
     * The runner:
     * <ul>
     *   <li>Prompts the user to enter the session initiator name</li>
     *   <li>Creates a {@link SessionManager}</li>
     *   <li>Delegates session handling to the {@code SessionManager}</li>
     * </ul>
     *
     * @param sessionService the service responsible for session operations
     * @return a {@link CommandLineRunner} that initiates the session process
     */
    @Bean
    CommandLineRunner commandLineRunner(SessionService sessionService) {
        return args -> {
            String initiator = CommandLineInput.initiateCommandLine();
            SessionManager sessionManager = new SessionManager(sessionService);
            sessionManager.handleSession(initiator);
        };
    }
}
