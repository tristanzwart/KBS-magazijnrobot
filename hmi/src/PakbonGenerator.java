import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PakbonGenerator {
    private String verzender;
    private String ontvanger;
    private String datum;
    private List<String[]> items;

    // Constructor
    public PakbonGenerator(String verzender, String ontvanger, List<String[]> items) {
        this.verzender = verzender;
        this.ontvanger = ontvanger;
        this.datum = formatDate();
        this.items = items;
    }

    // Methode om de pakbon tekstbestand te genereren
    public void genereerPakbon(String bestandsnaam) {
        try (FileWriter schrijver = new FileWriter(bestandsnaam)) {
            // Schrijf de header
            schrijver.write("Pakbon\n");
            schrijver.write("====================\n");
            schrijver.write("Verzender: " + verzender + "\n");
            schrijver.write("Ontvanger: " + ontvanger + "\n");
            schrijver.write("Datum: " + datum + "\n");
            schrijver.write("====================\n");
            schrijver.write(String.format("%-10s | %-20s | %-8s\n", "Artikelcode", "Omschrijving", "Aantal"));
            schrijver.write("-----------------------------\n");

            // Schrijf de items
            for (String[] item : items) {
                schrijver.write(String.format("%-10s | %-20s | %-8s\n", item[0], item[1], item[2]));
            }

            System.out.println("Pakbon succesvol aangemaakt.");
        } catch (IOException e) {
            System.err.println("Er is een fout opgetreden bij het schrijven van de pakbon.");
            e.printStackTrace();
        }
    }
    public String formatDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(dateTimeFormatter);  //17-02-2022
        return formattedDate;
    }
}

