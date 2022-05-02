package battleship;

import java.util.*;

public class PlayingField {

    private final Random random = new Random();
    protected Ship[][] shipsOnField;
    protected int[][] field;
    private static int square;
    protected final int HEIGHT_OF_FIELD;
    protected final int WIDTH_OF_FIELD;

    PlayingField(int width, int height) {
        HEIGHT_OF_FIELD = height;
        WIDTH_OF_FIELD = width;
        shipsOnField = new Ship[height][width];
        field = new int[height][width];
        square = width * height;
    }

    private boolean checkThePossibilityOfPlacingShips(int[] numbersOfShips) {
        int sum = 0;
        for (int i = 0; i < numbersOfShips.length; ++i) {
            sum += numbersOfShips[i] * (numbersOfShips.length - i);
        }
        double num = 0.5 * (double) square;
        if (square > sum + num && sum != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Установка корабля в рандомное место на игровом поле.
     *
     * @param numbersOfShips - массив, содержащий количество кораблей каждого вида.
     */
    public boolean placeShipsOnTheFieldRandomly(int[] numbersOfShips) {
        int numberOfAttempts = 0;
        if (checkThePossibilityOfPlacingShips(numbersOfShips)) {
            boolean isFulfilled = getRandomPlacesToShips(numbersOfShips);
            while (isFulfilled && numberOfAttempts < 5) {
                isFulfilled = getRandomPlacesToShips(numbersOfShips);
                ++numberOfAttempts;
            }
            if (!isFulfilled) {
                return true;
            } else {
                System.out.println("Enter new parameters the computer " +
                        "was unable to place ships on the field");
                return false;
            }
        } else {
            System.out.println("Enter the game parameters again as " +
                    "it is impossible to create a game with these parameters");
            return false;
        }
    }

    private boolean getRandomPlacesToShips(int[] numbersOfShips) {
        try {
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < numbersOfShips.length; ++i) {
                for (int j = 0; j < numbersOfShips[i]; ++j) {
                    int lengthOfShip = numbersOfShips.length - i;
                    boolean isVert = random.nextBoolean();
                    findPointForShip(points, lengthOfShip, isVert);
                    setUpTheShipOnTheField(points, lengthOfShip, isVert);
                    points.clear();
                }
            }
            return false;
        } catch (Exception e) {
            field = new int[HEIGHT_OF_FIELD][WIDTH_OF_FIELD];
            return true;
        }
    }

    private void setUpTheShipOnTheField(List<Point> points, int length, boolean isVert) {
        int index = random.nextInt(0, points.size());
        int count = 0;
        Ship shipToPaste = getShipType(length);
        if (isVert) {
            for (int r = points.get(index).x; count < length; ++r) {
                field[r][points.get(index).y] = 1;
                shipsOnField[r][points.get(index).y] = shipToPaste;
                shipToPaste.Points[count] = new Point(r, points.get(index).y);
                ++count;
            }
        } else {
            for (int r = points.get(index).y; count < length; ++r) {
                field[points.get(index).x][r] = 1;
                shipsOnField[points.get(index).x][r] = shipToPaste;
                shipToPaste.Points[count] = new Point(points.get(index).x, r);
                ++count;
            }
        }
        getBoundariesOfShip(points.get(index), length, isVert);
    }

    private Ship getShipType(int length) {
        Ship result = null;
        try {
            switch (length) {
                case 1:
                    result = new Submarine();
                    break;
                case 2:
                    result = new Destroyer();
                    break;
                case 3:
                    result = new Cruiser();
                    break;
                case 4:
                    result = new Battleship();
                    break;
                case 5:
                    result = new Carrier();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + length);
            }
        } catch (Exception e) {
            System.out.println("Unexpected value");
        }
        return result;
    }

    private void findPointForShip(List<Point> points, int length, boolean isVert) {
        for (int k = 0; k < HEIGHT_OF_FIELD; ++k) {
            for (int l = 0; l < WIDTH_OF_FIELD; ++l) {
                if (isVert) {
                    selectPointsForVerticalShip(k, l, length, points);
                } else {
                    selectPointsForHorizontalShip(k, l, length, points);
                }
            }
        }
    }

    private void selectPointsForHorizontalShip(int firstIndex, int secondIndex, int length, List<Point> points) {
        int count = 0;
        boolean isBreak = true;
        for (int h = secondIndex; count < length; ++h) {
            if ((h + length) > WIDTH_OF_FIELD || field[firstIndex][h] == 1 || field[firstIndex][h] == 2) {
                isBreak = false;
                break;
            }
            ++count;
        }
        if (isBreak && (secondIndex + length) <= WIDTH_OF_FIELD) {
            points.add(new Point(firstIndex, secondIndex));
        }
    }

    private void selectPointsForVerticalShip(int firstIndex, int secondIndex, int length, List<Point> points) {
        int count = 0;
        boolean isBreak = true;
        for (int h = firstIndex; count < length; ++h) {
            if ((h + length) > HEIGHT_OF_FIELD || field[h][secondIndex] == 1 || field[h][secondIndex] == 2) {
                isBreak = false;
                break;
            }
            ++count;
        }
        if (isBreak && (firstIndex + length) <= HEIGHT_OF_FIELD) {
            points.add(new Point(firstIndex, secondIndex));
        }
    }

    private void getBoundariesOfShip(Point point, int size, boolean isVert) {
        int borderLength = size + 2;
        int borderHeight = 3;
        if (isVert) {
            fillTheBoundaries(point, borderLength, borderHeight);
        } else {
            fillTheBoundaries(point, borderHeight, borderLength);
        }
    }

    private void fillTheBoundaries(Point point, int borderHeight, int borderLength) {
        int x = point.x;
        int y = point.y;
        int firstIndexer = 0;
        for (int i = x - 1; firstIndexer < borderHeight; ++i) {
            int secondIndexer = 0;
            for (int j = y - 1; secondIndexer < borderLength; ++j) {
                if (i >= 0 && j >= 0 && j < WIDTH_OF_FIELD
                        && i < HEIGHT_OF_FIELD && field[i][j] != 1) {
                    field[i][j] = 2;
                }
                ++secondIndexer;
            }
            ++firstIndexer;
        }
    }
}