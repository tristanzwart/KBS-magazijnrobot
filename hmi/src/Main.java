import java.awt.*;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();
        ArduinoCom a = new ArduinoCom();
        a.verstuurData("1800", "/dev/ttyACM0");

        ArduinoCom b = new ArduinoCom();
        b.verstuurData("-1800", "/dev/ttyACM1");

        a.verstuurData("1000", "/dev/ttyACM0");

    }

}
