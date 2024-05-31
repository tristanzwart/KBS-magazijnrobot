import java.util.ArrayList;
import java.util.List;

public class TSPAlgorimte {

    public double afstandberkenen(String punt1, String punt2) {
        int xas1, yas1, xas2, yas2;

        if (punt1.equals("start")) {
            xas1 = 0;
            yas1 = 0;
        } else {
            xas1 = Integer.parseInt(ArduinoCom.getCoordinates(punt1.charAt(0)));
            yas1 = Integer.parseInt(ArduinoCom.getCoordinates(punt1.charAt(1)));
        }
        if (punt2.equals("start")) {
            xas2 = 0;
            yas2 = 0;
        } else {
            xas2 = Integer.parseInt(ArduinoCom.getCoordinates(punt2.charAt(0)));
            yas2 = Integer.parseInt(ArduinoCom.getCoordinates(punt2.charAt(1)));
        }

        return Math.sqrt(Math.pow(xas1 - xas2, 2) + Math.pow(yas1 - yas2, 2));
    }

    public ArrayList<String> kortsteroute(List<String> puntenLijst) {
        ArrayList<String> bestRoute = null;
        double shortestDistance = Double.MAX_VALUE;

        String[] pointsArray = puntenLijst.toArray(new String[0]);
        List<List<String>> permutations = generatePermutations(pointsArray);

        for (List<String> permutation : permutations) {
            double distance = calculateTotalDistance(permutation);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestRoute = new ArrayList<>(permutation);
                bestRoute.add("60");
            }
        }
        for (String location : bestRoute) {
            System.out.println(location);
        }
        System.out.println("volgende");


        return bestRoute;
    }

    private double calculateTotalDistance(List<String> route) {
        double distance = 0;
        distance += afstandberkenen("start", route.get(0));
        for (int i = 0; i < route.size() - 1; i++) {
            distance += afstandberkenen(route.get(i), route.get(i + 1));
        }
        distance += afstandberkenen(route.get(route.size() - 1), "start");
        return distance;
    }

    private List<List<String>> generatePermutations(String[] points) {
        List<List<String>> result = new ArrayList<>();
        backtrack(points, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(String[] points, List<String> current, List<List<String>> result) {
        if (current.size() == points.length) {
            result.add(new ArrayList<>(current));
        } else {
            for (String point : points) {
                if (!current.contains(point)) {
                    current.add(point);
                    backtrack(points, current, result);
                    current.remove(current.size() - 1);
                }
            }
        }
    }

    public List<String[]> BinToTSP(List<Bin> verpakteBoxen) {
        List<String[]> puntenlijsten = new ArrayList<>();


        String[] puntenlijst = new String[3];
        int index = 0;


        for (Bin bin : verpakteBoxen) {
            for (List<Integer> item : bin.getItems()) {

                String locatie = Database.getlocatie(item.get(0));


                puntenlijst[index] = locatie;
                index++;


                if (index == 3) {
                    puntenlijsten.add(puntenlijst);
                    puntenlijst = new String[3];
                    index = 0;
                }
            }
        }


        if (index > 0) {
            puntenlijsten.add(puntenlijst);
        }

        return puntenlijsten;
    }

    public List<String[]> calculateAllRoutes(List<Bin> verpakteBoxen) {
        List<String[]> puntenlijsten = BinToTSP(verpakteBoxen);
        List<String[]> kortstepuntenlijsten = new ArrayList<>();

        for (String[] puntenlijst : puntenlijsten) {
            List<String> puntenLijst = new ArrayList<>();
            for (String punt : puntenlijst) {
                if (punt != null) {
                    puntenLijst.add(punt);
                }
            }

            if (!puntenLijst.isEmpty()) {
                ArrayList<String> bestRoute = kortsteroute(puntenLijst);
                kortstepuntenlijsten.add(bestRoute.toArray(new String[0]));
            }
        }

        return kortstepuntenlijsten;
    }
}