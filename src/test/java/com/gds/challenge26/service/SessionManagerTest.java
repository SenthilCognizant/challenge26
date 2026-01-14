package com.gds.challenge26.service;

import com.gds.challenge26.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SessionManagerTest {
    private SessionService sessionService;
    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionService = mock(SessionService.class);
        sessionManager = new SessionManager(sessionService);
    }

    @Test
    void handleSession_InitiatorNotPresent_ShouldThrowUnauthorizedAccessException() {
        String initiator = "Alice";

        // Mock sessionService to say user is not present
        when(sessionService.isUserPresent(initiator)).thenReturn(false);

//        when(sessionService.getUsers()).thenReturn(List.of(new UserDto("Alice"),new UserDto("BOB")));

        // Redirect System.in to simulate user input (not used here but required)
        InputStream sysInBackup = System.in;
        System.setIn(new ByteArrayInputStream(new byte[0]));

        // Expect UnauthorizedAccessException
        UnauthorizedAccessException exception = assertThrows(
                UnauthorizedAccessException.class,
                () -> sessionManager.handleSession(initiator)
        );

        assertEquals("Entered session initiator not authorized.", exception.getMessage());

        // Verify session ended
        verify(sessionService, times(1)).endSession();

        System.setIn(sysInBackup);
    }

    @Test
    void sessionOperation_SubmitResponse_ShouldCallService() {
        String participantName = "Alice";
        String restaurant = "Pizza Place";

        // Simulate scanner input for participant name and restaurant
        String input = participantName + "\n" + restaurant + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sessionManager.sessionOperation(2);

        verify(sessionService, times(1)).submitResponse(participantName, restaurant);
    }

    @Test
    void sessionOperation_EndSessionByInitiator_ShouldCallService() {
        String initiatorName = "Bob";

        String input = initiatorName + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sessionManager.sessionOperation(4);

        verify(sessionService, times(1)).endSessionByInitiator(initiatorName);
    }

    @Test
    void sessionOperation_InvalidChoice_ShouldNotCallService() {
        // Simulate scanner input for invalid choice
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        sessionManager.sessionOperation(99);

        // No service method should be called
        verifyNoInteractions(sessionService);
    }
}
