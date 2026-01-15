package com.gds.challenge26.controller;

import com.gds.challenge26.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    private SessionService sessionService;
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        sessionService = Mockito.mock(SessionService.class);
        sessionController = new SessionController(sessionService);
    }

    @Test
    void invite_shouldReturnInviteMessage() {
        String result = sessionController.invite("Alice");
        assertEquals("Alice invited.", result);
        verify(sessionService).invite("Alice");
    }

    @Test
    void submit_shouldReturnSubmitMessage() {
        String result = sessionController.submit("Bob", "Pizza");
        assertEquals("Response submitted.", result);
        verify(sessionService).submitResponse("Bob", "Pizza");
    }

    @Test

    void endSession_shouldReturnEndMessage() {
        String result = sessionController.endSession("Admin");
        assertEquals("Meeting ended by Admin", result);
        verify(sessionService).endSessionByInitiator("Admin");
    }

    @Test

    void pick_whenResponseExists_shouldReturnPickedAnswer() {
        when(sessionService.randomlyPickAnswer()).thenReturn("Alice: Sushi");
        String result = sessionController.pick();
        assertEquals("Alice: Sushi", result);
        verify(sessionService).randomlyPickAnswer();
    }

    @Test

    void pick_whenNoResponse_shouldReturnFallbackMessage() {
        when(sessionService.randomlyPickAnswer()).thenReturn(null);
        String result = sessionController.pick();
        assertEquals("No response or meeting ongoing.", result);
        verify(sessionService).randomlyPickAnswer();

    }

    @Test
    void participants_shouldReturnParticipantsList() {
        List<String> responses = List.of("Alice: Pizza", "Bob: Burger");
        when(sessionService.restaurantChoices()).thenReturn(responses);
        List<String> result = sessionController.participants();
        assertEquals(responses, result);
        verify(sessionService).restaurantChoices();
    }

}

