import java.rmi.Remote;
import java.rmi.RemoteException;

// Définition de l'interface CentraleInterface qui étend l'interface Remote
public interface CentraleInterface extends Remote {
    
    // Déclaration de la méthode recevoirDonnees qui peut être appelée à distance
    void recevoirDonnees(String id, double temperature, double humidite, double latitude, double longitude) throws RemoteException;
}