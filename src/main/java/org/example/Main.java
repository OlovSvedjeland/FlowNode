package org.example;

import org.example.Project;
import org.example.Task;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Project project = new Project();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> removeTask();
                case "3" -> addDependency();
                case "4" -> calculateSchedule();
                case "5" -> editTask();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("1. Add task");
        System.out.println("2. Remove task");
        System.out.println("3. Add dependency");
        System.out.println("4. Calculate schedule");
        System.out.println("5. Edit task (earliest start + required time)");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private static void addTask() {

        System.out.print("Task description: ");
        String description = sc.nextLine();
        project.addTask(description);
        System.out.println("Add earliest start (type 's' to skip or digits for earliest start)");
        String earliestStart = sc.nextLine();
        Task t = project.getTaskByDescription(description);

        if (!earliestStart.equals("s")) {
            int newEarliest = readInt(earliestStart);
            project.setEarliestStart(t, newEarliest);
        }

        System.out.println("Add required time for task (type 's' to skip");

        Task task = project.getTaskByDescription(description);

        System.out.println("Task added.");


        int newRequired = readInt("New required time: ");

    }

    private static void removeTask() {
        System.out.print("Task description to remove: ");
        String description = sc.nextLine();
    }

    private static void addDependency() {
        System.out.print("Parent task description: ");
        String parentDesc = sc.nextLine();

        System.out.print("Child task description: ");
        String childDesc = sc.nextLine();

        Task parent = project.getTaskByDescription(parentDesc);
        Task child = project.getTaskByDescription(childDesc);

        if (parent == null || child == null) {
            System.out.println("Parent or child not found.");
            return;
        }

        project.addDependency(child, parent);

        System.out.println("Dependency added " + childDesc + " depends on " + parentDesc);
    }

    private static void calculateSchedule() {
        System.out.println("\nCalculating schedule...");
        project.recomputeAllTasks();

        System.out.println("Result:");
        for (Task task : project.getAllTasks()) {
            System.out.println(
                    "Task: " + task.getDescription() +
                            " | EarliestStart: " + task.getEarliestStart() +
                            " | Required: " + task.getRequiredTime() +
                            " | ComputedStart: " + task.getComputedStart() +
                            " | End: " + task.getRequiredTime()
            );
        }
    }

    private static void editTask() {
        System.out.print("Task description: ");
        String description = sc.nextLine();

        Task task = project.getTaskByDescription(description);
        if (task == null) {
            System.out.println("Task not found.");
            return;
        }

        int newEarliest = readInt("New earliest start (baseLineStart): ");
        int newRequired = readInt("New required time: ");

        task.setEarliestStart(newEarliest);
        task.setRequiredTime(newRequired);

        System.out.println("Task updated.");
    }

    // Hjälper dig slippa krascha på NumberFormatException hela tiden
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}