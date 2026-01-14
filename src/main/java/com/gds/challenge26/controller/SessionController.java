package com.gds.challenge26.controller;

import com.gds.challenge26.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@Tag(name="Sessions",description="Manage restaurant voting sessions")
public class SessionController {
    private final SessionService sessionService ;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary="Join an active session")
    @PostMapping("/invite")
    public String invite(@RequestParam String participant) {
        sessionService.invite(participant);
        return participant + " invited.";
    }

    @Operation(summary="submit participant restaurant response")
    @PostMapping("/submit")
    public String submit(@RequestParam String participant, @RequestParam String restaurant) {
        sessionService.submitResponse(participant, restaurant);
        return "Response submitted.";
    }

    @Operation(summary="End the session")
    @GetMapping("/end")
    public String endSession(@RequestParam String initiator) {
        sessionService.endSessionByInitiator(initiator);
        return "Meeting ended by " + initiator;
    }

    @Operation(summary="Randomly pick an answer")
    @GetMapping("/pick")
    public String pick() {
        String picked = sessionService.randomlyPickAnswer();
        return picked != null ? picked : "No response or meeting ongoing.";
    }

    @Operation(summary="Get meeting participants")
    @GetMapping("/participants")
    public List<String> Participants(){
        return sessionService.restaurantChoices();
    }
}
