package battleship;

public class Submarine extends Ship {
    Submarine() {
        this.Length = 1;
        this.ShipType = "Submarine";
        this.Points = new Point[Length];
        this.Hit = new boolean[Length];
    }
}
