package battleship;

public class Cruiser extends Ship {
    Cruiser() {
        this.Length = 3;
        this.ShipType = "Cruiser";
        this.Points = new Point[Length];
        this.Hit = new boolean[Length];
    }
}
