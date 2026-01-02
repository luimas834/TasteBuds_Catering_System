package org.TBCS.util;

import java. math.BigDecimal;
import java.util.Scanner;

public class ConsoleHelper {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printHeader(String title) {
        System.out.println();
        System.out.println("+=========================================================+");
        System.out. println("|" + centerText(title, 57) + "|");
        System.out.println("+=========================================================+");
    }

    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println("+--------------------------------------------------+");
        System.out. println("|" + centerText(title, 50) + "|");
        System.out.println("+--------------------------------------------------+");
    }

    public static void printDivider() {
        System.out.println("--------------------------------------------------");
    }

    public static void printSuccess(String message) {
        System.out. println("\n[SUCCESS] " + message);
    }

    public static void printError(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        int rightPadding = width - text. length() - padding;
        return " ". repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, rightPadding));
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer. parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError("Invalid number. Please try again.");
            }
        }
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            printError("Please enter a number between " + min + " and " + max);
        }
    }

    public static BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError("Invalid amount. Please try again.");
            }
        }
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input. equals("y") || input.equals("yes")) return true;
            if (input. equals("n") || input.equals("no")) return false;
            printError("Please enter 'y' or 'n'.");
        }
    }

    public static void waitForEnter() {
        System.out. print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}