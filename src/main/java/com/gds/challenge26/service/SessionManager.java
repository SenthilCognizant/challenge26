package com.gds.challenge26.service;

import com.gds.challenge26.exception.UnauthorizedAccessException;
import com.gds.challenge26.model.UserDto;
import com.gds.challenge26.util.MenuProvider;

import java.util.List;
import java.util.Scanner;

public class SessionManager {
    private final SessionService sessionService;

    public SessionManager(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Handles the lifecycle of a session initiated by the given user.
     * <p>
     * This method:
     * <ul>
     *   <li>Displays the list of available users</li>
     *   <li>Validates whether the initiator is authorized</li>
     *   <li>Initializes the session for a valid initiator</li>
     *   <li>Continuously displays a menu and processes user selections</li>
     * </ul>
     * If the initiator is not authorized, the session is terminated
     * and the application exits.
     *
     * @param initiatorName the name of the user attempting to start the session
     */
    public void handleSession(String initiatorName) {

        Scanner scanner = new Scanner(System.in);

        List<UserDto> users = sessionService.getUsers();
        System.out.print("List of users " + users); //for CLI purpose

        if(sessionService.isUserPresent(initiatorName)) {
            sessionService.initUser(initiatorName);
            while (true) {
                MenuProvider.displayMenu();

                int choice = 0;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                sessionOperation(choice);
            }
        }
        else{
            System.out.print("Entered session initiator not Authorized /n"); //for CLI purpose
            sessionService.endSession();
            System.out.println("Exiting the session application."); //for CLI purpose
            throw new UnauthorizedAccessException("Entered session initiator not authorized.");
        }
    }

    /**
     * Executes a session-related operation based on the user's menu selection.
     * <p>
     * Supported operations include:
     * <ul>
     *   <li>Inviting participants</li>
     *   <li>Submitting restaurant choices</li>
     *   <li>Viewing participant responses</li>
     *   <li>Ending the session</li>
     *   <li>Randomly selecting a restaurant</li>
     * </ul>
     *
     * @param choice the menu option selected by the user
     */
    public void sessionOperation(int choice) {

        Scanner scanner = new Scanner(System.in);

        switch (choice) {
            case 1:
                System.out.print("Enter the name of the participant to invite: "); //for CLI purpose
                String participant = scanner.nextLine();
                sessionService.invite(participant);
                break;
            case 2:
                System.out.print("Enter your name (participant): "); //for CLI purpose
                String responder = scanner.nextLine();
                System.out.print("Enter your choice of restaurant: "); //for CLI purpose
                String response = scanner.nextLine();
                sessionService.submitResponse(responder, response);
                break;
            case 3:
                System.out.print("Participant and the choice of restaurant"); //for CLI purpose
                sessionService.restaurantChoices();
                break;
            case 4:
                System.out.print("Enter your name (initiator) to end the session: "); //for CLI purpose
                String endInitiator = scanner.nextLine();
                sessionService.endSessionByInitiator(endInitiator);
                break;
            case 5:
                sessionService.randomlyPickAnswer();
                break;
            case 6:
                System.out.println("Exiting the session application."); //for CLI purpose
                System.exit(0);
            default:
                System.out.println("Invalid option. Please enter to retry."); //for CLI purpose
                scanner.nextLine(); // Consume the newline character
        }
    }
}
