package Centrale;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;

public class CentraleMetier  {
    private HashMap<Integer, CapteurInterface> capteurs;
    private ScheduledExecutorService executor;

    public CentraleMetier() {
        this.capteurs = new HashMap<Integer, CapteurInterface>();
    }

    //Methodes
    public void registerCapteur(int id) throws MalformedURLException, RemoteException, NotBoundException, CapteurInknowException {
        CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:4444/" + id);
        this.capteurs.put(id, capteur);
        receiveData(id);
    }

    public void unregisterCapteur(int id) throws RemoteException {
        if (this.capteurs.containsKey(id)) {
            this.capteurs.remove(id);
            System.out.println("Capteur " + id + " retiré.");
        }
    }

    public void activerCapteur(int id) throws RemoteException, CapteurInknowException {
        if (this.capteurs.containsKey(id) && !this.capteurs.get(id).getEstActif()) {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.activer();
            receiveData(id);
            System.out.println("Capteur " + id + " activé.");
        }
    }

    public void desactiverCapteur(int id) throws RemoteException, CapteurInknowException {
        if (this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()) {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.desactiver();
            executor.shutdown();
            System.out.println("Capteur " + id + " désactivé.");
        }
    }

    public void afficherData(int id) throws RemoteException {
        if (this.capteurs.containsKey(id)) {
            CapteurInterface capteur = this.capteurs.get(id);
            System.out.println("Capteur " + id + " : " + capteur.getTemperature() + "°C, " + capteur.getHumidite() + "%");
        }
    }

    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException {
        if (id == 0) {
            capteurs.values().forEach(capteur -> {
                try {
                    System.out.print(capteur.getId());
                    capteur.setInterval(intervalle);
                    executor.shutdown();
                    receiveData(capteur.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            System.out.println("Interval modifié pour tous les capteurs : " + intervalle + " secondes");
        } else {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.setInterval(intervalle);
            executor.shutdown();
            receiveData(id);
            System.out.println("Interval modifié pour le capteur " + id + " : " + intervalle + " secondes");
        }
    }

    private void receiveData(int id) throws CapteurInknowException, RemoteException{
        executor = Executors.newSingleThreadScheduledExecutor();
        if(this.capteurs.containsKey(id)){
            CapteurInterface capteur = this.capteurs.get(id);
            Runnable task = () -> {
                while (true) {
                    try {
                        LocalDateTime now = LocalDateTime.now();
                        String formatter = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                        //ecrire dans un fichier
                        FileWriter writer = new FileWriter("data.txt", true);
                        writer.write("[" + formatter + "] Capteur " + id + " : " + capteur.getTemperature() + "°C, " + capteur.getHumidite() + "%\n");
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
    
    public HashMap<Integer, CapteurInterface> getCapteurs() throws RemoteException {
        return this.capteurs;
    }

    public CapteurInterface getLastInfoCapteur(int id) throws RemoteException {
        return this.capteurs.get(id);
    }
}
