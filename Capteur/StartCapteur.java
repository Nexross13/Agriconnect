package Capteur;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class StartCapteur {
    public static void main(String[] args) throws RemoteException {
        int id = Math.random() > 0 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1;
        id = id + 1;
        CapteurInterface nouveauCapteur = new CapteurImpl(id);
        try {
            Naming.rebind("rmi://localhost:4444/capteur" + id , nouveauCapteur);
            System.out.println("Capteur " + id + " démarré [Latitude: " + nouveauCapteur.getLatitude() + " | Longitude: " + nouveauCapteur.getLongitude() + "].");
        } catch (Exception e) {
            System.err.println("Exception lors du démarrage du capteur: " + e.toString());
        }
    }
}
