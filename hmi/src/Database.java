import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static String url = "jdbc:mysql://localhost:3306/nerdygadgets";
    private static String user = "root";
    private static String password = "";

    public Database() {

    }
    /*
    public void allStockItems () {
        try (Connection conn = DriverManager.getConnection(url, user, password); // Maak verbinding met de database
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM stockitems")) { // Voer de sql statement uit

            // Haal het aantal kolommen op
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Itereer door de resultaatset
            while (rs.next()) {
                // Print elke kolom van de rij
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String value = rs.getString(i);
                    System.out.print(columnName + ": " + value + " ");
                }
                // Print een nieuwe regel aan het einde van elke rij
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Fout bij het ophalen van stockitems");
        }
    }
    */
    public String[] getorderinfo(int orderid) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String[] info = new String[3];
            String statement = "SELECT OrderID, CustomerID, OrderDate FROM orders WHERE OrderID= ?;";
            PreparedStatement mystmt = conn.prepareStatement(statement);
            mystmt.setInt(1, orderid);
            ResultSet myres = mystmt.executeQuery();

            if (myres.next()) {
                String orderID = myres.getString("OrderID");
                String customerID = myres.getString("CustomerID");
                String orderDate = myres.getString("OrderDate");


                info[0] = orderID;
                info[1] = customerID;
                info[2] = orderDate;
            } else {

                System.out.println("No order found for OrderID: " + orderid);
            }

            return info;
        } catch (SQLException e) {

            System.out.println("ophalen order info is niet gelukt");
            return null;
        }
    }
    public Object[][] getOrderlines(int OrderID) {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT StockItemID, Description, Quantity FROM orderlines WHERE OrderID = ?")) {
            pstmt.setInt(1, OrderID); // Set the OrderID parameter in the prepared statement

            try (ResultSet rs = pstmt.executeQuery()) {
                int index=1;
                while (rs.next()) {
                    Object[] row = new Object[4]; // Number of columns is 3
                    if(row[0]== null){
                        row[0]= index;
                        index++;
                    }

                    for (int i = 1; i <= 3; i++) {
                        row[i] = rs.getObject(i);
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order lines: " + e.getMessage());
        }
        return rows.toArray(new Object[0][]);
    }

    public Object[][] getStockItems() {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT h.StockItemLocation, s.StockItemID, h.QuantityOnHand, StockItemName FROM stockitems s JOIN stockitemholdings h ON s.StockItemID = h.StockItemID")) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[4]; //4 is het aantal kolommen dit staat vast

                for (int i = 1; i <= 4; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching stock items");
        }
        return rows.toArray(new Object[0][]);
    }

    public Object[][] getOrders() {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT o.OrderId, o.CustomerID, SUM(l.Quantity) AS TotalQuantity, o.Comments FROM orders o JOIN orderlines l ON o.OrderID = l.OrderID GROUP BY o.OrderId, o.CustomerID, o.Comments LIMIT 300")) {


            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[4]; //4 is het aantal kolommen dit staat vast

                for (int i = 1; i <= 4; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order items");
        }
        return rows.toArray(new Object[0][]);
    }

    public void addStockItem(String Productnaam, int QuantityOnHand, String locatie) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Query voor het toevoegen van een rij aan de tabel 'stockitems'
            String queryStockItems = "INSERT INTO stockitems (Productnaam, locatie) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(queryStockItems)) {
                pstmt.setString(1, Productnaam);
                pstmt.setString(2, locatie);
                pstmt.executeUpdate();
            }

            // Query voor het toevoegen van een rij aan de tabel 'stockitemholdings'
            String queryStockItemHoldings = "INSERT INTO stockitemholdings (Productnaam, QuantityOnHand) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(queryStockItemHoldings)) {
                pstmt.setString(1, Productnaam);
                pstmt.setInt(2, QuantityOnHand);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Toevoegen van Artikel is mislukt");
        }
    }

    public static void updateStockItem(int stockItemId, int newQuantityOnHand) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Query voor het bijwerken van de 'QuantityOnHand' van een specifiek 'StockItemID'
            String query = "UPDATE stockitemholdings SET QuantityOnHand = ? WHERE StockItemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, newQuantityOnHand);
                pstmt.setInt(2, stockItemId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Bijwerken van Artikel is mislukt");
        }
    }


}
