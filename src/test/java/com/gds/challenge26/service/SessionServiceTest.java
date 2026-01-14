package com.gds.challenge26.service;

import com.gds.challenge26.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionServiceTest {


        private UserRepository userRepository;
        private SessionService sessionService;

        @BeforeEach
        void setUp() {
            userRepository = Mockito.mock(UserRepository.class);
            sessionService = new SessionService(userRepository);
        }

        @Test
        void shouldReturnTrueIfUserExists() {
            when(userRepository.existsByName("Yang")).thenReturn(true);

            boolean exists = sessionService.isUserPresent("Yang");

            assertTrue(exists);
            verify(userRepository).existsByName("Yang");
        }

        @Test
        void shouldInviteParticipantWhenSessionNotEnded() {
            sessionService.invite("Wen Yuan");

            assertEquals(List.of("Wen Yuan"), sessionService.getParticipants());
        }

        @Test
        void shouldSubmitResponseForValidParticipant() {
            sessionService.invite("Ying Ying");

            sessionService.submitResponse("Ying Ying", "Chicken Rice");

            assertEquals(1, sessionService.getResponses().size());
            assertEquals("Ying Ying: Chicken Rice", sessionService.getResponses().get(0));
        }

        @Test
        void shouldEndSessionByInitiator() {
            sessionService.initUser("Admin");

            sessionService.endSessionByInitiator("Admin");

            assertTrue(sessionService.isEnded());
        }

        @Test
        void shouldPickRandomAnswerAfterSessionEnds() {
            sessionService.initUser("Wei Wen");
            sessionService.invite("Sam");
            sessionService.submitResponse("Sam", "Bak kut teh");
            sessionService.endSessionByInitiator("Wei Wen");

            String picked = sessionService.randomlyPickAnswer();

            assertNotNull(picked);
            assertTrue(picked.contains("Bak kut teh"));
        }
}

