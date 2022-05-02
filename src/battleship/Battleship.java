package battleship;

public class Battleship extends Ship {
    Battleship() {
        this.Length = 4;
        this.ShipType = "Battleship";
        this.Points = new Point[Length];
        this.Hit = new boolean[Length];
    }
}
