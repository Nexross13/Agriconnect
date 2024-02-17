import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Définition de la classe CentraleGestion qui implémente l'interface CentraleInterface
public class CentraleGestion extends UnicastRemoteObject implements CentraleInterface {

    // Constructeur de la classe CentraleGestion
    public CentraleGestion() throws RemoteException {
        super();
    }

    // Méthode recevoirDonnees de la class CentraleInterface
    public void recevoirDonnees(String id, double temperature, double humidite, double latitude, double longitude) throws RemoteException {
        System.out.println("Capteur " + id + " connecté");
        System.out.println("Capteur " + id + ": Température = " + temperature + ", Humidité = " + humidite + ", Latitude = " + latitude + ", Longitude = " + longitude);
        System.out.println("Capteur " + id + " déconnecté");

    // Obtention de la date et de l'heure actuelles
    LocalDateTime now = LocalDateTime.now();
    // Formatage de la date et de l'heure en une chaîne de caractères
    String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Ajout des données dans un fichier
        try (FileWriter writer = new FileWriter("donnees.txt", true)) {
            writer.write(formattedNow + " - Capteur " + id + ": Température = " + temperature + ", Humidité = " + humidite + ", Latitude = " + latitude + ", Longitude = " + longitude + "\n");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'écriture dans le fichier : " + e.toString());
        }
    }

    // Méthode main
    public static void main(String[] args) {
        try {
            // Création d'une instance de CentraleGestion
            CentraleInterface stub = (CentraleInterface) new CentraleGestion();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("Centrale", stub);

            // Affichage message pour indiquer que la centrale est prête
            System.out.println("Centrale prête.");
        } catch (Exception erreur) {

            // Affichage d'un message d'erreur en cas d'exception
            System.err.println("Exception de la centrale: " + erreur.toString());
            erreur.printStackTrace();
        }
    }
}