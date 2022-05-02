package battleship;

import java.util.Scanner;

public class Main {
    public static final int NUMBER_OF_ARGUMENTS = 7;
    public static Scanner scanner = new Scanner(System.in);

    private static void displayCloseOffer() {
        System.out.println("If you see this message, you have an error in the input data for the game.");
        System.out.println("Do you want to continue playing or want to close the application?");
        String input;
        do {
            System.out.println("If you want to continue write continue if you exit - exit");
            input = scanner.nextLine();
        } while (!input.equals("continue") && !input.equals("exit"));

        if (input.equals("exit")) {
            System.exit(0);
        }
    }

    private static int[] parseStringArray(String[] arguments) {
        int[] arrayOfArguments = new int[arguments.length];
        try {
            for (int i = 0; i < arguments.length; ++i) {
                arrayOfArguments[i] = Integer.parseInt(arguments[i]);
            }
        } catch (Exception e) {
            System.out.println("The entered string does not match the format");
            arrayOfArguments = new int[0];
        }
        return arrayOfArguments;
    }

    private static int[] getIntegerArray(String[] args) {
        while (args.length != NUMBER_OF_ARGUMENTS) {
            displayCloseOffer();
            System.out.println("The number of arguments on the command " +
                    "line does not match the number of arguments");
            System.out.println("Enter the arguments again separated by a space");
            args = scanner.nextLine().split(" ");
        }
        return parseStringArray(args);
    }

    private static boolean checkArrayForNegativeNumbers(int[] arguments) {
        for (int argument : arguments) {
            if (argument < 0) {
                return true;
            }
        }
        return false;
    }

    private static int[] checkArrayForCorrectData(int[] arguments) {
        String[] line;
        while (arguments.length != NUMBER_OF_ARGUMENTS || checkArrayForNegativeNumbers(arguments)) {
            displayCloseOffer();
            System.out.println("Enter the arguments again separated by a space");
            line = scanner.nextLine().split(" ");
            arguments = parseStringArray(line);
        }
        return arguments;
    }

    private static int[] getArrayWithNumberOfShips(int[] arguments) {
        int[] array = new int[NUMBER_OF_ARGUMENTS - 2];
        System.arraycopy(arguments, 2, array, 0, array.length);
        return array;
    }

    /**
     * Метод запуска программы.
     *
     * @param args- аргументы командной строки.
     */
    public static void main(String[] args) {
        int[] arguments = checkArrayForCorrectData(getIntegerArray(args));
        PlayingField battleField = new PlayingField(arguments[0], arguments[1]);

        int[] arrayOfTheNumberOfShips = getArrayWithNumberOfShips(arguments);
        while (!battleField.placeShipsOnTheFieldRandomly(arrayOfTheNumberOfShips)) {
            displayCloseOffer();
            System.out.println("Enter arguments again");
            args = scanner.nextLine().split(" ");
            arguments = checkArrayForCorrectData(getIntegerArray(args));
            battleField = new PlayingField(arguments[0], arguments[1]);
            arrayOfTheNumberOfShips = getArrayWithNumberOfShips(arguments);
        }

        System.out.println("Ships are placed let's start playing!");
        System.out.println("1.Miss - ° ");
        System.out.println("2.Drowning - x ");
        System.out.println("3.Hit - ¤");
        System.out.println("4.Did not shoot - *\n");

        Game game = new Game(battleField);
        game.startGame();
    }
}

