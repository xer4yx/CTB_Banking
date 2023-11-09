package com.ctb.banking;

import com.ctb.classes.BankSystem;
import com.ctb.classes.Display;

import java.util.Scanner;

public class DriverCode {

    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        new BankSystem("bank_data.json"); //TODO: delete this
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
                    Display.forgotPassword();
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
