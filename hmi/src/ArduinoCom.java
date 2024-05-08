import com.fazecast.jSerialComm.*;

public class ArduinoCom {

    public void verstuurData(String data, String comPoort){
        SerialPort comPort = SerialPort.getCommPort(comPoort); // Gebruikt de Linux seriële poort. Voor windows gebruik COM poort
        comPort.setBaudRate(9600);
        if (!comPort.openPort()) {
            System.out.println("Kan poort niet openen!");
            return;
        }

        try {
            // Wacht 2 seconden voor het versturen van commando's om verbinding te stabiliseren
            Thread.sleep(2000);

            // Verzend een commando naar de Arduino
            String command = data + "\n"; // Vraag de temperatuur op
            comPort.getOutputStream().write(command.getBytes());
            comPort.getOutputStream().flush();

            // Wacht en lees het antwoord
            while (comPort.bytesAvailable() == 0)
                Thread.sleep(20);

            byte[] readBuffer = new byte[comPort.bytesAvailable()];
            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
            System.out.println("Ontvangen van Arduino: " + new String(readBuffer, 0, numRead));

        } catch (InterruptedException e) {
            System.out.println("Een fout opgetreden tijdens het wachten: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();
    }


}
