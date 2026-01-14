package com.gds.challenge26.util;

import java.util.Scanner;

public class CommandLineInput {
    public static String initiateCommandLine() {
        Scanner scanner = new Scanner(System.in); //CLI
        System.out.print("Enter the name of the session initiator: "); //for CLI purpose
        return scanner.nextLine();
    }
}
