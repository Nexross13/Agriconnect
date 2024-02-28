import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) {
        CentraleMetier centraleMetier = new CentraleMetier();
        
        try {
            CentraleImpl centraleImpl = new CentraleImpl(centraleMetier);

            LocateRegistry.createRegistry(4444);

            Naming.rebind("rmi://localhost:4444/centrale", centraleImpl);// permet d'associer un nom à un objet
            System.out.println("Serveur lancé");

        } catch (RemoteException er) {
            er.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
