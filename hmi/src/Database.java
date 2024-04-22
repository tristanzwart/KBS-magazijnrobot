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

    public Object[][] getStockItems() {
        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT StockItemID, ColorID, StockItemName FROM stockitems")) { //TODO: Query is nog niet correct

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[4]; //4 is het aantal kolommen dit staat vast

                for (int i = 1; i <= 4; i++) {
                    if(i == 1){
                        row[0] = "DUMMY DATA";
                    }
                    row[i-1] = rs.getObject(i-1);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching stock items");
        }
        return rows.toArray(new Object[0][]);
    }
    /*
    public void allOrders () {
        try (Connection conn = DriverManager.getConnection(url, user, password); // Maak verbinding met de database
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) { // Voer de sql statement uit

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
            System.out.println("Fout bij het ophalen van orders");
        }
    }
    */
}
