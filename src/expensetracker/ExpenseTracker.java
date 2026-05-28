package expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Expense implements Serializable {
    double amount;
    String category;
    LocalDate date;

    Expense(double amount, String category, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Amount: " + amount +
                ", Category: " + category +
                ", Date: " + date;
    }
}

public class ExpenseTracker {
    static ArrayList<Expense> expenses = new ArrayList<>();
    static final String FILE_NAME = "expenses.dat";

    public static void addExpense(Scanner sc) {
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateInput = sc.nextLine();

        LocalDate date = LocalDate.parse(dateInput);

        expenses.add(new Expense(amount, category, date));

        System.out.println("Expense Added Successfully!");
    }

    public static void displayExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }

        System.out.println("\n--- Expense Records ---");
        for (Expense e : expenses) {
            System.out.println(e);
        }
    }

    public static void generateMonthlyReport(Scanner sc) {
        System.out.print("Enter Month (1-12): ");
        int month = sc.nextInt();

        double total = 0;

        for (Expense e : expenses) {
            if (e.date.getMonthValue() == month) {
                total += e.amount;
            }
        }

        System.out.println("Total Expense for Month " + month + ": " + total);
    }

    public static void highestExpenseCategory() {
        HashMap<String, Double> categoryTotal = new HashMap<>();

        for (Expense e : expenses) {
            categoryTotal.put(
                    e.category,
                    categoryTotal.getOrDefault(e.category, 0.0) + e.amount
            );
        }

        String highestCategory = "";
        double max = 0;

        for (Map.Entry<String, Double> entry : categoryTotal.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        if (!highestCategory.isEmpty()) {
            System.out.println("Highest Expense Category: " +
                    highestCategory + " = " + max);
        }
    }

    public static void saveExpenses() {
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(FILE_NAME));

            out.writeObject(expenses);
            out.close();

            System.out.println("Expenses Saved Successfully!");
        } catch (Exception e) {
            System.out.println("Error Saving File.");
        }
    }

    public static void loadExpenses() {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(FILE_NAME));

            expenses = (ArrayList<Expense>) in.readObject();
            in.close();

            System.out.println("Expenses Loaded Successfully!");
        } catch (Exception e) {
            System.out.println("No Previous Data Found.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        loadExpenses();

        int choice;

        do {
            System.out.println("\n===== Personal Expense Tracker =====");
            System.out.println("1. Add Expense");
            System.out.println("2. Display Expenses");
            System.out.println("3. Monthly Report");
            System.out.println("4. Highest Expense Category");
            System.out.println("5. Save Expenses");
            System.out.println("6. Exit");

            System.out.print("Enter Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addExpense(sc);
                    break;

                case 2:
                    displayExpenses();
                    break;

                case 3:
                    generateMonthlyReport(sc);
                    break;

                case 4:
                    highestExpenseCategory();
                    break;

                case 5:
                    saveExpenses();
                    break;

                case 6:
                    saveExpenses();
                    System.out.println("Exiting Application...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 6);

        sc.close();
    }
}