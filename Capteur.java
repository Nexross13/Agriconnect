import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



// Définition de la classe Capteur
public class Capteur {
    private String id; // Identifiant unique du capteur
    private double latitude; // Latitude du capteur
    private double longitude; // Longitude du capteur
    private double temperature; // Température enregistrée par le capteur
    private double humidite; // Humidité enregistrée par le capteur

    // Constructeur de la classe Capteur
    public Capteur(String id, double latitude, double longitude) {
        this.id = id; // Initialisation de l'identifiant
        this.latitude = latitude; // Initialisation de la latitude
        this.longitude = longitude; // Initialisation de la longitude
    }

    private void mesurerTemperature() {
        // Simulation de la mesure de température
        this.temperature = Math.round(Math.random() * 30 * 10) / 10.0; // On créer un nombre aléatoire entre 0 et 30, puis on arrondi la valeur aléatoire à 1 chiffre après la virgule
    }

    private void mesurerHumidite() {
        // Simulation de la mesure d'humidité
        this.humidite = Math.round(Math.random() * 100 * 10) / 10.0; // Nombre aléaoire entre 0 et 100
    }


    // Méthode pour envoyer des données
    public void connexionCentrale() {
        // Création d'un service d'exécution planifiée
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // Définition de la tâche à exécuter
        Runnable task = () -> {
            try {
                // Génération de données aléatoires pour la température et l'humidité
                mesurerTemperature();
                mesurerHumidite();

                Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
                
                // Recherche de la centrale dans le registre RMI
                CentraleInterface centrale = (CentraleInterface) registry.lookup("Centrale");
                // Envoi des données à la centrale
                centrale.recevoirDonnees(id, temperature, humidite, humidite, humidite);

                System.out.println("Données envoyées par le capteur " + id);
            } catch (Exception erreur) {
                // Affichage d'un message d'erreur si ça ne marche pas
                System.err.println("Erreur du capteur " + id);
            }
        };

        // Planification de la tâche pour qu'elle s'exécute toutes les 5 secondes
        executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }


    // Getter pour la longitude
    public String getId() {
        return id;
    } 
}