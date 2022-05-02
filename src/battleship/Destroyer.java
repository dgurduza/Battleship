package battleship;

public class Destroyer extends Ship {
    Destroyer() {
        this.Length = 2;
        this.ShipType = "Destroyer";
        this.Points = new Point[Length];
        this.Hit = new boolean[Length];
    }
}
