package Client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class StartClient {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, IOException {
        int id = Math.random() > 0 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1;
        ClientInterface nouveauClient = new ApplicationClient(id);
        try {
            Naming.rebind("rmi://localhost:4444/client", nouveauClient);
            System.out.println("Client démarré.");
        } catch (Exception e) {
            System.err.println("Exception lors du démarrage du client: " + e.toString());
        }

        System.out.println("Connexion au serveur...");
        nouveauClient.connecter();
        nouveauClient.demarrage();
    }
}
