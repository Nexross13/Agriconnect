import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Capteur {
    // Attributs
    private int id;
    private String name; // Nom du capteur
    private double latitude; // Latitude du capteur
    private double longitude; // Longitude du capteur
    private double temperature; // Température du capteur
    private double humidity; // Humidité du capteur

    // constructeur
    public Capteur(String name, double latitude, double longitude) {
        this.id = new Random().nextInt(100);
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTemperature() {
        makeTemperature();
        return temperature;
    }

    public double getHumidity() {
        makeHumidity();
        return humidity;
    }

    // Methodes
    public void makeTemperature(){
        this.temperature = Math.round(Math.random() * 30 * 10) / 10.0; // On créer un nombre aléatoire entre 0 et 30, puis on arrondi la valeur aléatoire à 1 chiffre après la virgule
    }

    public void makeHumidity(){
        this.humidity = Math.round(Math.random() * 100 * 10) / 10.0; // On créer un nombre aléatoire entre 0 et 100, puis on arrondi la valeur aléatoire à 1 chiffre après la virgule
    }

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Capteur capteur = new Capteur("Capteur2", 48.666, 2.777);

        Remote centraleLink;
        try {
            System.out.println("Connexion au serveur");
            centraleLink = Naming.lookup("rmi://localhost:4444/centrale");
            Centrale centrale = (Centrale) centraleLink;

            centrale.registerCapteur(capteur.getName(), capteur.getLatitude(), capteur.getLongitude());
            System.out.println("Capteur " + capteur.name + " enregistré");
            
            Runnable task = () -> {
                while (true) {
                    try {
                        centrale.receiveData(capteur.getName());
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (CapteurInknowException e) {
                        e.printStackTrace();
                    }
                }
            };

            executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);

        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
