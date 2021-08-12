import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner =  new Scanner(System.in);

    private static int numberSelection(int highBoundary){
        int chosenNumber;
        do{
            System.out.println("Select number in range [1;"+highBoundary+"] !!!");
            System.out.print("\nMake your choice: ");
            while (!scanner.hasNextInt()){
                System.out.println("I don't understand what does it mean! Input number from list.");
                scanner.next();
            }
            chosenNumber = scanner.nextInt();
        }while (chosenNumber < 1 || chosenNumber > highBoundary);
        return chosenNumber;
    }

    private static LocalDate parseDate(String date) throws DateTimeParseException{
        return LocalDate.parse(date);
    }

    private static String inputDate(){
        System.out.print("Input date in format yyyy-MM-dd : ");
        return scanner.next();
    }

    private static void printCase(){
        int paragraph;
        System.out.println("\nNow let's be more concrete:");
        do{
            System.out.print("\t1. Show all tasks\n\t2. Show tasks for certain day \n\t3. Back\n");
            paragraph = numberSelection(3);
            switch (paragraph){
                case 1:
                    try {
                        LinkedList<Task> tasks = QueryManager.selectAllQuery();
                        printLinkedList(tasks);
                    }catch (SQLException e){
                        System.out.println("An error occurred while connecting to database or running query");
                    }
                    break;

                case 2:
                    do {
                        try {
                            LocalDate date = parseDate(inputDate());
                            LinkedList<Task> tasks = QueryManager.recordsByDateQuery(date);
                            System.out.print("\n\n");
                            tasks.forEach(task -> System.out.println(task.toString()));
                            System.out.print("\n\n");
                            break;
                        } catch (SQLException e){
                            e.printStackTrace();
                            System.out.println("An error occurred while connecting to database or running query");
                        } catch (DateTimeParseException e){
                            System.out.println("\nInvalid date format!");
                        }
                    }while(true);
                    break;

                case 3:
                    break;
            }
        }while (paragraph != 3);
    }

    private static void addCase(){
        int paragraph;
        LocalDate date;
        String title;
        boolean status;
        do{
            scanner.nextLine();
            System.out.print("\nInput the title title: ");
            title = scanner.nextLine();
            do try {
                date = parseDate(inputDate());
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format!");
            } while (true);
            System.out.print("Input status {true/false}: ");
            while (!scanner.hasNextBoolean()){
                System.out.println("Invalid status!");
                scanner.next();
            }
            status = scanner.nextBoolean();
            QueryManager.insertQuery(title, date, status);
            System.out.println("Done!\n");
            System.out.println("Wanna add another one?");
            System.out.println("\t1. Yes.\n\t2. No, bring me back!");
            paragraph = numberSelection(2);
        }while (paragraph != 2);
    }

    private static void printLinkedList(LinkedList<Task> linkedList) {
        System.out.print("\n\n");
        for(Task listElement :linkedList)
            System.out.println(listElement.toString());
        System.out.print("\n\n");
    }

    private static void deleteCase(){
        int paragraph, taskId;
        do {
            try {
                LinkedList<Task> tasks = QueryManager.selectAllQuery();
                HashSet<Integer> idSet = new HashSet<>();
                tasks.forEach(task -> idSet.add(task.getId()));
                if(idSet.isEmpty()){
                    System.out.println("There's nothing to delete!");
                    return;
                }
                System.out.println("Available tasks:");
                printLinkedList(tasks);
                do{
                    System.out.print("Input ID from list!\nID: ");
                    while (!scanner.hasNextInt()){
                        System.out.println("I don't understand what does it mean! Input ID from list.");
                        scanner.next();
                    }
                    taskId = scanner.nextInt();
                }while (!idSet.contains(taskId));
                QueryManager.deleteQuery(taskId);
                System.out.println("There's one less to worry about!");
            }catch (SQLException e){
                System.out.println("An error occurred while connecting to database or running query");
            }
            System.out.println("Wanna delete another one?");
            System.out.println("\t1. Yes.\n\t2. No, bring me back!");
            paragraph = numberSelection(2);
        }while (paragraph != 2);
    }

    private static boolean exitAgreement(){
        System.out.print("\nSure you want to quit?\n\t1. Yes\n\t2. I'm going to stay\n");
        int choice = numberSelection(2);
        if (choice == 2){
            System.out.println("Got you, keep going");
        }
        return choice == 1;
    }

    private static void mainMenu(){
        int paragraph;
        boolean exit = false;
        do{
            System.out.println("\nWhat do we do?");
            System.out.print("\t1. Show task\n\t2. Add task\n\t3. Delete task\n\t4. Exit\n");
            paragraph = numberSelection(4);
            switch (paragraph){
                case 1:
                    printCase();
                    break;

                case 2:
                    addCase();
                    break;

                case 3:
                    deleteCase();
                    break;

                case 4:
                    exit = exitAgreement();
                    break;
            }
        }while (!exit);
    }

    public static void main(String[] args) {
        System.out.println("Hi, User! It's 'To Do List' application. Author: Anton Slyzhko");
        mainMenu();
        System.out.print("Well, goodbye!");
        scanner.close();
    }

}

