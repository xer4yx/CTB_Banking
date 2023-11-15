package com.ctb.banking;

import com.ctb.classes.BankSystem;
import com.ctb.classes.Display;

import java.util.Scanner;

public class DriverCode {
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        boolean isRunning = true;
        while (isRunning) {
            Display.displayMainMenu();
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    if (Display.loginUser()){
                       Display.handleDashboardOptions();
                    }
                    break;

                case 2:
                    Display.handleProductApplication();
                    break;

                case 3:
                    BankSystem.forgotPassword();
                    break;

                case 4:
                    isRunning = false;
                    break;

                default:
                    System.out.println("*Invalid choice. Please select a valid option.");
            }
        }
    }
}
