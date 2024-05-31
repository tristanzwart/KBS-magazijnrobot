import javax.swing.*;

public class Bevestigingsdialog  {
    private String tekst;
    private boolean antwoord = false;
    public void show(String tekst) {
        // Knop opties
        Object[] options = {"Ja", "Nee"};

        this.tekst = tekst;
        this.antwoord = false;
        int response = JOptionPane.showOptionDialog(null, this.tekst, "Waarschuwing",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (response == JOptionPane.YES_OPTION) {
            // Als optie ja gekozen
            System.out.println("Gebruiker bevestigde actie");
            this.antwoord = true;
        } else {
            // Als optie nee gekozen
            System.out.println("Gebruiker annuleerde actie");
            this.antwoord = false;
        }
    }

    public boolean antwoord() {
        return antwoord;
    }
}