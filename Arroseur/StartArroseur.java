package Arroseur;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class StartArroseur {
    public static void main(String[] args) throws RemoteException {
        System.out.print("Entrez l'ID du nouveau capteur: ");
        int id = Math.random() > 0 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1;
        id = id + 1;
        double latitude = Math.random() * 180 - 90;
        double longitude = Math.random() * 360 - 180;
        System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);

        ArroseurInterface nouveauArroseur = new ArroseurImpl(id, latitude, longitude);
        try {
            Naming.rebind("rmi://localhost:4444/" + id , nouveauArroseur);
            System.out.println("Arroseur " + id + " démarré.");
        } catch (Exception e) {
            System.err.println("Exception lors du démarrage de l'arroseur: " + e.toString());
        }
    }
}
