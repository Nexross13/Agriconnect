package Centrale;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class StartCentrale {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Démarrer le registre RMI sur le port 1100
            CentraleInterface centrale = new CentraleImpl();
            Naming.rebind("rmi://localhost/Centrale", centrale);
            System.out.println("Centrale prête.");
        } catch (Exception e) {
            System.err.println("Exception lors du démarrage de la centrale: " + e.toString());
            e.printStackTrace();
        }
    }
}

