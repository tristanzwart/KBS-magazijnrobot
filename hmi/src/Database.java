import java.sql.*;

public class Database {
    private static String url = "jdbc:mysql://localhost:3306/nerdygadgets";
    private static String user = "root";
    private static String password = "";

    public Database() {

    }

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
}
