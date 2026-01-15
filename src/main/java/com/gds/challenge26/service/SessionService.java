package com.gds.challenge26.service;

import com.gds.challenge26.model.User;
import com.gds.challenge26.model.UserDto;
import com.gds.challenge26.repository.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Service
public class SessionService {
    private String initiator;
    private final List<String> participants =new ArrayList<>();
    private final List<String> responses = new ArrayList<>();
    private boolean ended;

    private final UserRepository repository;

    public boolean isEnded(){
        return ended;
    }

    public List<String> getParticipants(){
        return participants;
    }

    public List<String> getResponses(){
        return responses;
    }

    public SessionService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDto> getUsers() {
       List<User> users =  repository.findAll();

        return users.stream()
        .map(user -> new UserDto(user.getName()))
        .toList();
    }

    public boolean isUserPresent(String name){
        return repository.existsByName(name);
    }

    public void initUser(String name){
        this.initiator = name;
    }

    /**
     * Invites a participant to the session.
     * <p>
     * Participants can only be invited while the session is active.
     *
     * @param participant the name of the participant to invite
     */
    public void invite(String participant) {
        if (!ended) {
            participants.add(participant);
            System.out.println(participant + " has been invited to the session."); //for CLI purpose
        } else {
            System.out.println("The session has ended. You cannot invite more participants."); //for CLI purpose
            throw new IllegalArgumentException("The session has ended. Cannot invite more participants.");
        }
    }

    /**
     * Submits a restaurant choice from a participant.
     * <p>
     * Responses are accepted only if the session is active and the participant
     * has been invited.
     *
     * @param participant the name of the participant submitting the response
     * @param response the selected restaurant choice
     */
    public void submitResponse(String participant, String response) {
        if (!ended && participants.contains(participant)) {
            responses.add(participant + ": " + response);
            System.out.println(participant + " submitted response: " + response); //for CLI purpose
        } else {
            System.out.println("Invalid participant or the session has ended."); //for CLI purpose
            throw new IllegalArgumentException("Participant " + participant + " is not part of the session.");
        }
    }

    /**
     * Ends the session if invoked by the session initiator.
     *
     * @param initiator the name of the user attempting to end the session
     */
    public void endSessionByInitiator(String initiator) {
        if (initiator.equals(this.initiator)) {
            System.out.println("The session has been ended by the initiator."); //for CLI purpose
            ended = true;
        } else {
            System.out.println("Only the initiator can end the session."); //for CLI purpose
        }
    }

    /**
     * Ends the session unconditionally.
     */
    public void endSession() {
            System.out.println("The session has been ended."); //for CLI purpose
            ended = true;
    }

    /**
     * Randomly selects a restaurant choice from submitted responses.
     * <p>
     * This operation is allowed only after the session has ended.
     *
     * @return the randomly selected response, or {@code null} if unavailable
     */
    public String randomlyPickAnswer() {
        if (ended) {
            if (responses.isEmpty()) {
                System.out.println("No responses submitted. Cannot pick an answer."); //for CLI purpose
                throw new IllegalArgumentException("No responses submitted. Cannot pick an answer.");
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(responses.size());
                String pickedAnswer = responses.get(randomIndex); //for CLI purpose
                System.out.println("Randomly picked answer: " + pickedAnswer);
                return pickedAnswer;
            }
        } else {
            System.out.println("The meeting is still ongoing. Cannot pick an answer yet."); //for CLI purpose
        }
        return null;
    }

    /**
     * Displays and returns all submitted restaurant choices.
     * <p>
     * Responses are available only while the session is active.
     *
     * @return the list of participant responses, or {@code null} if unavailable
     */
    public List<String> restaurantChoices() {
        if (!ended) {
            if (responses.isEmpty()) {
                System.out.println("\nNo responses submitted. Cannot pick an answer."); //for CLI purpose
                throw new IllegalArgumentException("No responses submitted. Cannot list choices.");
            } else {
                for(String choice : responses)
                    System.out.println("\nchoice: " + choice); //for CLI purpose
                return responses;
            }
        } else {
            System.out.println("The session ended."); //for CLI purpose
            throw new IllegalArgumentException("The session has ended.");
        }
    }
}
