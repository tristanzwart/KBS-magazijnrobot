import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Line_Panel extends JPanel {
    private String[] locaties;

    public Line_Panel(String[] locaties) {
        this.locaties = locaties;
        setOpaque(false); // Make the panel transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLines(g, locaties);
    }

    public void drawLines(Graphics g, String[] locaties) {
        if (locaties == null || locaties.length < 2) return;

        ArrayList<Integer> prevCoord = getLocaties(locaties[0]);
        for (int i = 1; i < locaties.length; i++) {
            ArrayList<Integer> currCoord = getLocaties(locaties[i]);

            g.drawLine(prevCoord.get(0), prevCoord.get(1), currCoord.get(0), currCoord.get(1));

            // Update previous coordinate to current
            prevCoord = currCoord;
        }
    }

    public static ArrayList<Integer> getLocaties(String location) {
        int afstandTussenLocaties = 200;
        int beginx = 140;
        int beginy = 200;
        ArrayList<Integer> xy = new ArrayList<Integer>();
        switch (location) {
            case "A1", "A2", "A3", "A4", "A5" -> xy.add(beginx);
            case "B1", "B2", "B3", "B4", "B5" -> xy.add(beginx + afstandTussenLocaties);
            case "C1", "C2", "C3", "C4", "C5" -> xy.add(beginx + (afstandTussenLocaties * 2));
            case "D1", "D2", "D3", "D4", "D5" -> xy.add(beginx + (afstandTussenLocaties * 3));
            case "E1", "E2", "E3", "E4", "E5" -> xy.add(beginx + (afstandTussenLocaties * 4));
        }

        switch (location) {
            case "A1", "B1", "C1", "D1", "E1" -> xy.add(beginy);
            case "A2", "B2", "C2", "D2", "E2" -> xy.add(beginy + afstandTussenLocaties);
            case "A3", "B3", "C3", "D3", "E3" -> xy.add(beginy + (afstandTussenLocaties * 2));
            case "A4", "B4", "C4", "D4", "E4" -> xy.add(beginy + (afstandTussenLocaties * 3));
            case "A5", "B5", "C5", "D5", "E5" -> xy.add(beginy + (afstandTussenLocaties * 4));
        }

        return xy;

    }
}

