package Centrale;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StartCentrale {
    public static void main(String[] args) throws IOException {
        CentraleMetier centraleMetier = new CentraleMetier();
        
        try {
            CentraleImpl centraleImpl = new CentraleImpl(centraleMetier);

            LocateRegistry.createRegistry(4444);

            Naming.rebind("rmi://localhost:4444/centrale", centraleImpl);// permet d'associer un nom à un objet
            LocalDateTime now = LocalDateTime.now();
            String formatter = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            FileWriter writer = new FileWriter("Logs/log.txt", true);
            writer.write("[" + formatter + "] : " + "Serveur lancé" + "%\n");
            writer.close();
            System.out.println("Serveur lancé");

        } catch (RemoteException er) {
            er.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}