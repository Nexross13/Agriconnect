package Centrale;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;

public class CentraleMetier {
    private HashMap<Integer, CapteurInterface> capteurs;

    public CentraleMetier() {
        this.capteurs = new HashMap<Integer, CapteurInterface>();
    }

    //Methodes
    public void registerCapteur(int id) throws MalformedURLException, RemoteException, NotBoundException, CapteurInknowException {
        CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:4444/" + id);
        this.capteurs.put(id, capteur);
        receiveData(id);
    }

    public void afficherData(int id) throws RemoteException {
        if (this.capteurs.containsKey(id)) {
            CapteurInterface capteur = this.capteurs.get(id);
            System.out.println("Capteur " + id + " : " + capteur.getTemperature() + "°C, " + capteur.getHumidite() + "%");
        }
    }

    private void receiveData(int id) throws CapteurInknowException, RemoteException{
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        if(this.capteurs.containsKey(id)){
            CapteurInterface capteur = this.capteurs.get(id);
            Runnable task = () -> {
                while (true) {
                    try {
                        //ecrire dans un fichier
                        FileWriter writer = new FileWriter("data.txt", true);
                        writer.write("Capteur " + id + " : " + capteur.getTemperature() + "°C, " + capteur.getHumidite() + "%\n");
                        writer.close();
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            executor.scheduleAtFixedRate(task, 0, capteur.getInterval(), TimeUnit.SECONDS);

        } else {
            throw new CapteurInknowException();
        }
    }
}
