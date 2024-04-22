import java.awt.*;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();

        Database db = new Database();
        db.allOrders();
    }
}
