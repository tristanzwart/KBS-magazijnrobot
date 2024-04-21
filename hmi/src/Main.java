import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/nerdygadgets";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from countries")) {

            if (rs.next()) {
                String kaas = rs.getString("CountryName");
                System.out.println("Kaas: " + kaas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
