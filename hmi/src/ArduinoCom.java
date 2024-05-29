import com.fazecast.jSerialComm.*;

public class ArduinoCom {
    private SerialPort comPort;
    private int arduinoNummer;
    private StringBuilder dataBuilder = new StringBuilder();

    public ArduinoCom(String comPoort, int arduinoNummer) {
        comPort = SerialPort.getCommPort(comPoort); // Gebruikt de Linux seriële poort. Voor windows gebruik COM poort
        comPort.setBaudRate(9600);
        if (!comPort.openPort()) {
            System.out.println("Kan poort niet openen!");
            return;
        }

        try{
            // Wacht 2 seconden voor het versturen van commando's om verbinding te stabiliseren
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Een fout opgetreden tijdens het wachten: " + e.getMessage());
        }

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                dataBuilder.append(new String(newData, 0, numRead));
                if (dataBuilder.toString().contains("\n")) {
                    String dataLine = dataBuilder.toString().split("\n")[0];
                    dataBuilder.delete(0, dataLine.length() + 1);

                    //Print de ontvangen data voor debug doeleinden
                    System.out.println("Ontvangen van Arduino" + arduinoNummer + ": " + dataLine);

                }
            }
        });
    }


    public void verstuurData(String data){


        try {
            // Verzend een commando naar de Arduino
            String command = data + "\n"; // Vraag de temperatuur op
            comPort.getOutputStream().write(command.getBytes());
            comPort.getOutputStream().flush();

            // Wacht en lees het antwoord
//            while (comPort.bytesAvailable() == 0)
//                Thread.sleep(20);
//
//            byte[] readBuffer = new byte[comPort.bytesAvailable()];
//            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
//            System.out.println("Ontvangen van Arduino: " + new String(readBuffer, 0, numRead));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closePort() {
        comPort.closePort();
    }

    public static String getCoordinates(char choice) {

        switch (choice) {

            case 'A':
                return String.valueOf(-1701);
            case 'B':
                return String.valueOf(-2475);
            case 'C':
                return String.valueOf(-3140);
            case 'D':
                return String.valueOf(-3800);
            case 'E':
                return String.valueOf(-4534);
            case '1':
                return String.valueOf(181);
            case '2':
                return String.valueOf(700);
            case '3':
                return String.valueOf(1200);
            case '4':
                return String.valueOf(1653);
            case '5':
                return String.valueOf(2238);
            case '0':
                return String.valueOf(0);
            default:
                return "Invalid choice";
        }
    }
}
