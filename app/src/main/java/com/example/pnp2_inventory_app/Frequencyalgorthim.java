package com.example.pnp2_inventory_app;

//Firstly, the program includes necessary import statements and defines a constant variable for the file path of the item list. It also declares a main method, which serves as the entry point for the program.
//
//The main method initializes an empty list called itemList to store items and loads any existing items from a JSON file using the loadItemsFromFile method. It also creates a Scanner object to read user input.
//
//Inside the program's menu loop, the user is presented with several options. They can choose to add an item, display the current list of items, show the current date and time, generate a shopping list based on specific criteria, or exit the program.
//
//When the user selects to add an item, the addItem method is called. It prompts the user to enter the item name, expiration date, and amount. It then validates the input and checks if the item already exists in the list. If it exists, the item's frequency and amount are updated accordingly. If it's a new item, a new Item object is created and added to the itemList. Additionally, if the expiration date is within two weeks, the item's frequency is increased.
//
//If the user chooses to display the item list, the displayItemList method is called. It checks if the list is empty and prints the details of each item, including the name, expiration date, and amount.
//
//The option to show the current date and time invokes the showCurrentDateTime method. It retrieves the current date and time using the LocalDateTime class and formats it before displaying.
//
//The generateShoppingList method is responsible for generating a recommended shopping list based on specific criteria. It iterates over each item in the itemList, compares its frequency and expiration date with predefined conditions, and adds the eligible items to a sorted list. The sorting is based on the item's frequency in descending order. Finally, the sorted list is displayed, showing the item name, expiration date, and amount.
//
//To handle file operations, the program includes two additional methods: loadItemsFromFile and saveItemsToFile. The former reads item details from a file (specified by the FILE_PATH) and populates the itemList. The latter writes the item details from the itemList back to the file.
//
//Once the user selects the exit option, the itemList is saved to the file using saveItemsToFile, and the program terminates.




import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class Main {
    private static final String FILE_PATH = "items.json";

    public static void main(String[] args) {
        List<Item> itemList = new ArrayList<>(); // List to store items
        loadItemsFromFile(itemList);
        Scanner scanner = new Scanner(System.in);

        // Menu loop
        boolean exit = false;
        while (!exit) {
            System.out.println("--- Item List Menu ---");
            System.out.println("1. Add item");
            System.out.println("2. Display items");
            System.out.println("3. Show current date and time");
            System.out.println("4. Generate shopping list");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addItem(scanner, itemList);
                    break;
                case 2:
                    displayItemList(itemList);
                    break;
                case 3:
                    showCurrentDateTime();
                    break;
                case 4:
                    generateShoppingList(itemList);
                    break;
                case 5:
                    saveItemsToFile(itemList);
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println();
        }

        // Close the scanner
        scanner.close();
    }

    public static void generateShoppingList(List<Item> itemList) {
        List<Item> sortedList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        for (Item item : itemList) {
            LocalDate expirationDate = LocalDate.parse(item.getExpirationDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            long weeksUntilExpiration = ChronoUnit.WEEKS.between(currentDate, expirationDate);

            if (item.getFrequency() >= 20 && weeksUntilExpiration <= 3) {
                double frequency = calculateFrequency(item.getFrequency(), weeksUntilExpiration);
                item.setFrequency(frequency);

                sortedList.add(item);
            }
        }

        Collections.sort(sortedList, (a, b) -> Double.compare(b.getFrequency(), a.getFrequency()));

        System.out.println("Recommended Shopping List:");

        for (Item item : sortedList) {
            System.out.println("Item Name: " + item.getItemName());
            System.out.println("Expiration Date: " + item.getExpirationDate());
            System.out.println("Amount: " + item.getAmount());
            //System.out.println("Frequency: " + item.getFrequency());
            System.out.println();
        }
    }

    public static double calculateFrequency(double currentFrequency, long weeksUntilExpiration) {
        double frequency = currentFrequency;
        if (weeksUntilExpiration >= 1) {
            frequency -= Math.pow(2, (3 - weeksUntilExpiration));
        }
        return frequency;
    }


    public static void addItem(Scanner scanner, List<Item> itemList) {
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter expiration date (yyyy-MM-dd): ");
        String expirationDateString = scanner.nextLine();

        try {
            LocalDate expirationDate = LocalDate.parse(expirationDateString, DateTimeFormatter.ISO_LOCAL_DATE);

            // Calculate the difference between expiration date and current date
            LocalDate currentDate = LocalDate.now();
            long daysUntilExpiration = ChronoUnit.DAYS.between(currentDate, expirationDate);

            System.out.print("Enter amount: ");
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Check if item already exists in the list
            boolean itemExists = false;
            for (Item item : itemList) {
                if (item.getItemName().equalsIgnoreCase(itemName)) {
                    item.setFrequency(item.getFrequency() + amount);
                    item.setAmount(item.getAmount() + amount);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                Item newItem = new Item(itemName, expirationDate.toString(), amount, amount);
                itemList.add(newItem);
                System.out.println("New item added:");
                System.out.println(newItem.getItemName());
            }
            else
            {
                System.out.println("Item already exists. Frequency and amount increased by " + amount + ".");
            }

            // Check if expiration date is within two weeks
            if (daysUntilExpiration <= 14) {
                // Add 4 points to frequency
                for (Item item : itemList) {
                    if (item.getItemName().equalsIgnoreCase(itemName)) {
                        item.setFrequency(item.getFrequency() + 4);
                        break;
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid amount. Please enter a valid integer.");
            scanner.nextLine(); // Consume the invalid input
        } catch (Exception e) {
            System.out.println("Invalid expiration date format. Please enter in the format yyyy-MM-dd.");
        }
    }

    public static void displayItemList(List<Item> itemList) {
        if (itemList.isEmpty()) {
            System.out.println("Item list is empty.");
        } else {
            System.out.println("Item List:");
            for (Item item : itemList) {
                System.out.println("Item Name: " + item.getItemName());
                System.out.println("Expiration Date: " + item.getExpirationDate());
                System.out.println("Amount: " + item.getAmount());
                //System.out.println("Frequency: " + item.getFrequency());
                System.out.println();
            }
        }
    }

    public static void showCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("Current Date and Time: " + formattedDateTime);
    }

    public static void loadItemsFromFile(List<Item> itemList) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String itemName = data[0];
                String expirationDate = data[1];
                int amount = Integer.parseInt(data[2]);
                double frequency = Double.parseDouble(data[3]);
                itemList.add(new Item(itemName, expirationDate, amount, frequency));
            }
        } catch (IOException e) {
            System.out.println("Error occurred while loading items from file.");
        }
    }

    public static void saveItemsToFile(List<Item> itemList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Item item : itemList) {
                writer.write(item.getItemName() + "," + item.getExpirationDate() + "," +
                        item.getAmount() + "," + item.getFrequency());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error occurred while saving items to file.");
        }
    }
}





