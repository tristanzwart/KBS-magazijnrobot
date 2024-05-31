import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Database {
    private static String url = "jdbc:mysql://localhost:3306/nerdygadgets";
    private static String user = "root";
    private static String password = "";


    public Database() {

    }

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

                System.out.println("Geen order gevonden voor OrderID: " + orderid);
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
             PreparedStatement pstmt = conn.prepareStatement("SELECT OrderLineID, StockItemID, Description, Quantity FROM orderlines WHERE OrderID = ?")) {
            pstmt.setInt(1, OrderID); // Goede OrderID meenemen

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[4]; // 4 kolommen
                    for (int i = 1; i <= 4; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error met ophalen orderlines: " + e.getMessage());
        }
        return rows.toArray(new Object[0][]);
    }


    public Object[][] getStockItems() {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT h.StockItemLocation, s.StockItemID, h.QuantityOnHand, StockItemName FROM stockitems s JOIN stockitemholdings h ON s.StockItemID = h.StockItemID WHERE h.StockItemLocation IS NOT NULL ORDER BY StockItemID");) {

            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                Object[] row = new Object[4]; //4 is het aantal kolommen dit staat vast

                for (int i = 1; i <= 4; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error met ophalen stock items");
        }
        return rows.toArray(new Object[0][]);
    }

    public Object[][] getOrders() {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT o.OrderId, o.CustomerID, SUM(l.Quantity) AS TotalQuantity, o.Comments FROM orders o JOIN orderlines l ON o.OrderID = l.OrderID WHERE o.InternalComments != 'IsPicked' GROUP BY o.OrderId, o.CustomerID, o.Comments LIMIT 100")) {

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                Object[] row = new Object[rsmd.getColumnCount()];

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error met ophalen order items");
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

    public static void updateOrderLine(int OrderID, int newQuantity, int OrderLineID) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Query voor het bijwerken van de 'QuantityOnHand' van een specifiek 'StockItemID'
            String query = "UPDATE orderlines SET Quantity = ? WHERE OrderID = ? AND OrderLineID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, OrderID);
                pstmt.setInt(3, OrderLineID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Updaten van Orderline is mislukt!");
        }
    }

    public static void addOrderLine(int OrderID, int Quantity, int StockItemID) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Toevoegen van orderline
            String query = "INSERT INTO orderlines (OrderID, StockItemID, Description, PackageTypeID, Quantity, TaxRate, PickedQuantity, LastEditedBy, LastEditedWhen) VALUES (?, ?, (select StockItemName from stockitems where StockItemID = ?), 4, ?, 15.0, 0, 3, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, OrderID);
                pstmt.setInt(2, StockItemID);
                pstmt.setInt(3, StockItemID);
                pstmt.setInt(4, Quantity);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formatDateTime = now.format(formatter);
                pstmt.setString(5, formatDateTime);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Toevoegen orderline mislukt");
        }
    }

    public static void deleteOrderLine(int orderLineID) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Verwijder orderline
            String query = "DELETE FROM orderlines WHERE OrderLineID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, orderLineID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Deleting order line failed!");
        }
    }

    public static String getStockItemName(int StockItemID) {
        String stockItemName = null;
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT StockItemName FROM stockitems WHERE StockItemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, StockItemID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        stockItemName = rs.getString("StockItemName");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error met ophalen stock item naam");
        }
        return stockItemName;
    }

    public static String getlocatie(int StockItemID) {
        String stockItemloc = null;
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT StockItemLocation FROM stockitemholdings WHERE StockItemID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, StockItemID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        stockItemloc = rs.getString("StockItemLocation");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error met ophalen stock item locatie");
        }
        return stockItemloc;
    }


    public List<List<Integer>> voorBinPacking(int OrderID) {
        List<List<Integer>> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT o.StockItemID, o.Quantity, s.InternalComments FROM orderlines o JOIN stockitems s ON o.StockItemID = s.StockItemID WHERE o.OrderID = ?")) {
            pstmt.setInt(1, OrderID); // OrderID meegeven

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    List<Integer> row = new ArrayList<>(3);
                    row.add(rs.getInt("StockItemID"));
                    row.add(rs.getInt("Quantity"));
                    row.add(Integer.parseInt(rs.getString("InternalComments")));

                    result.add(row);



                }
            }
        } catch (SQLException e) {
            System.out.println("Error met ophalen order lines: " + e.getMessage());
        }
        return result;
    }


    public String[] getOrderLineInfo(int orderid, int stockItemID) {
        String[] info = new String[3];

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String statement = "SELECT StockItemID, Description, Quantity FROM orderlines WHERE OrderID = ? && StockItemID = ?;";
            PreparedStatement mystmt = conn.prepareStatement(statement);
            mystmt.setInt(1, orderid);
            mystmt.setInt(2, stockItemID);
            ResultSet myres = mystmt.executeQuery();

            if (myres.next()) {

                String description = myres.getString("Description");
                String quantity = myres.getString("Quantity");

                info[0] = String.valueOf(stockItemID);
                info[1] = description;
                info[2] = quantity;
            } else {
                System.out.println("No order lines found for OrderID: " + orderid);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve order line info");
            e.printStackTrace();
            return null;
        }

        return info;
    }

    public String getcustomername(int Orderid) {
        String customerName = null;
        String statement = "SELECT CustomerName FROM customers WHERE CustomerID IN (SELECT CustomerID FROM orders WHERE OrderID = ?);";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement mystmt = conn.prepareStatement(statement)) {

            mystmt.setInt(1, Orderid);
            try (ResultSet myres = mystmt.executeQuery()) {
                if (myres.next()) {
                    customerName = myres.getString("CustomerName");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve customer name");
            e.printStackTrace();
        }
        return customerName;
    }


    public static void VoorraadVerlagen(int OrderID) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Query voor het bijwerken van de 'QuantityOnHand' van een specifiek 'StockItemID'
            String query = "UPDATE stockitemholdings SET QuantityOnHand = QuantityOnHand - 1 WHERE StockItemID IN (SELECT StockItemID FROM orderlines WHERE OrderID = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, OrderID);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Updaten van Orderline is mislukt!");
        }
    }
}


