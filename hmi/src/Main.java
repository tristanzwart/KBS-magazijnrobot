import java.awt.*;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();
        ArduinoCom a = new ArduinoCom();
        a.verstuurData("1200");
    }

}
