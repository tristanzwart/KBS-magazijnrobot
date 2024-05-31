import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel(){
        setPreferredSize(new Dimension(1800, 917));
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


    }

}
