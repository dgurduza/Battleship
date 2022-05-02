package battleship;

public class Game {

    private final char CELL_SYMBOL = '*';
    private final char SHOT_SYMBOL = '¤';
    private final char MISS_SYMBOL = '°';
    private final char SYMBOL_OF_SUNNED_SHIP = 'x';
    private final char[][] fieldOnDisplay;
    private final PlayingField battleField;
    private int numberOfShots;

    Game(PlayingField field) {
        numberOfShots = 0;
        battleField = field;
        fieldOnDisplay = new char[battleField.HEIGHT_OF_FIELD][battleField.WIDTH_OF_FIELD];
        fillingDisplayField();
    }

    private void fillingDisplayField() {
        for (int i = 0; i < fieldOnDisplay.length; ++i) {
            for (int j = 0; j < fieldOnDisplay[0].length; ++j) {
                fieldOnDisplay[i][j] = CELL_SYMBOL;
            }
        }
    }

    /**
     * Запуск игрового процесса.
     */
    public void startGame() {
        do {
            System.out.println("Battle field:");
            displayField();
            Point coordinatesOfHit = getCoordinates();
            numberOfShots++;
            searchForShipByCoordinates(coordinatesOfHit);
        } while (checkFieldForShips());
        finishTheGame(numberOfShots);
    }

    private void finishTheGame(int numberOfShots) {
        for (int i = 0; i < battleField.HEIGHT_OF_FIELD; ++i) {
            for (int j = 0; j < battleField.WIDTH_OF_FIELD; ++j) {
                if (fieldOnDisplay[i][j] == CELL_SYMBOL) {
                    fieldOnDisplay[i][j] = MISS_SYMBOL;
                }
            }
        }
        displayField();
        System.out.printf("You fired %d shot(s)", numberOfShots);
    }

    private void displayField() {
        for (int i = 0; i < battleField.HEIGHT_OF_FIELD + 1; ++i) {
            for (int j = 0; j < battleField.WIDTH_OF_FIELD + 1; ++j) {
                if (i == 0 && j != 0) {
                    System.out.print(" " + j);
                } else if (j == 0 && i != 0) {
                    System.out.print(i + " ");
                } else if (i != 0 && j != 0) {
                    System.out.print(fieldOnDisplay[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
    }

    private Point getCoordinates() {
        String[] coordinates;
        boolean isCorrect;
        int[] array = new int[2];
        do {
            System.out.println("Enter ship coordinates separated by a space:\nExample: x y");
            coordinates = Main.scanner.nextLine().split(" ");
            if (coordinates.length == 2) {
                isCorrect = false;
                array = getIntegerArray(coordinates);
                if (array.length == 0 || checkArrayToNegativeNumbers(array)
                        || checkCoordinatesForComplianceWithTheCondition(array)) {
                    isCorrect = true;
                }
            } else {
                isCorrect = true;
                System.out.println("Enter ship coordinates again");
            }
        } while (isCorrect);
        return new Point(array[0], array[1]);
    }

    private boolean checkCoordinatesForComplianceWithTheCondition(int[] array) {
        for (int argument : array) {
            if (argument >= battleField.WIDTH_OF_FIELD ||
                    argument >= battleField.HEIGHT_OF_FIELD) {
                return true;
            }
        }
        return false;
    }

    private int[] getIntegerArray(String[] coordinates) {
        int[] arrayOfCoordinates = new int[coordinates.length];
        try {
            for (int i = 0; i < coordinates.length; ++i) {
                arrayOfCoordinates[i] = Integer.parseInt(coordinates[i]) - 1;
            }
        } catch (Exception e) {
            System.out.println("The entered string does not match the format");
            arrayOfCoordinates = new int[0];
        }
        return arrayOfCoordinates;
    }

    private boolean checkArrayToNegativeNumbers(int[] coordinates) {
        for (int argument : coordinates) {
            if (argument < 0) {
                return true;
            }
        }
        return false;
    }

    private void searchForShipByCoordinates(Point point) {
        if (battleField.shipsOnField[point.x][point.y] != null) {
            hitShip(battleField.shipsOnField[point.x][point.y], point);
            battleField.shipsOnField[point.x][point.y] = null;
        } else {
            if (fieldOnDisplay[point.x][point.y] == CELL_SYMBOL) {
                fieldOnDisplay[point.x][point.y] = MISS_SYMBOL;
                System.out.println("Miss");
            } else {
                System.out.println("You have already shot this cage");
            }
        }
    }

    private void hitShip(Ship ship, Point point) {
        int count = 0;
        for (int i = 0; i < ship.Hit.length; ++i) {
            count++;
            if (!ship.Hit[i]) {
                ship.Hit[i] = true;
                System.out.println("HIT!!");
                fieldOnDisplay[point.x][point.y] = SHOT_SYMBOL;
                break;
            }
        }
        if (count == ship.Length) {
            System.out.printf("You sunk a %s-type ship\n", ship.ShipType);
            redrawingTheSunkenShip(ship);
        }
    }

    private void redrawingTheSunkenShip(Ship ship) {
        for (int i = 0; i < ship.Points.length; ++i) {
            fieldOnDisplay[ship.Points[i].x][ship.Points[i].y] = SYMBOL_OF_SUNNED_SHIP;
        }
    }

    private boolean checkFieldForShips() {
        for (int i = 0; i < battleField.HEIGHT_OF_FIELD; ++i) {
            for (int j = 0; j < battleField.WIDTH_OF_FIELD; ++j) {
                if (battleField.shipsOnField[i][j] != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
