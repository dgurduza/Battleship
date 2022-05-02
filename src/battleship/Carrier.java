package battleship;

public class Carrier extends Ship {
    Carrier() {
        this.Length = 5;
        this.ShipType = "Carrier";
        this.Points = new Point[Length];
        this.Hit = new boolean[Length];
    }
}
