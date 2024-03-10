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
    private void ecrireLog(String message) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String formatter = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        FileWriter writer = new FileWriter("Logs/capteur.log", true);
        writer.write("[" + formatter + "] : " + message);
        writer.close();
    }

    public void registerCapteur(int id) throws MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException {
        CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:4444/" + id);
        this.capteurs.put(id, capteur);
        String message = "Le capteur " + id + " a été ajouté à la centrale" + "\n";
        ecrireLog(message);
        receiveData(id);
    }

    public void unregisterCapteur(int id) throws RemoteException {
        if (this.capteurs.containsKey(id)) {
            this.capteurs.remove(id);
        }
    }

    public void activerCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        if (this.capteurs.containsKey(id) && !this.capteurs.get(id).getEstActif()) {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.activer();
            String message = "Le capteur " + id + " a été activé" + "\n";
            ecrireLog(message);
            receiveData(id);
        }
    }

    public void desactiverCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        if (this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()) {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.desactiver();
            String message = "Le capteur " + id + " a été désactivé" + "\n";
            ecrireLog(message);
            executor.shutdown();
        }
    }

    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException, IOException {
        if (id == 0) {
            capteurs.values().forEach(capteur -> {
                try {
                    capteur.setInterval(intervalle);
                    executor.shutdown();
                    String message = "L'intervalle de tous les capteurs a été modifié à " + intervalle + " secondes " + "\n";
                    ecrireLog(message);
                    receiveData(capteur.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.setInterval(intervalle);
            executor.shutdown();
            String message = "L'intervalle du capteur " + id + " a été modifié à " + intervalle + " secondes " + "%\n";
            ecrireLog(message);
            receiveData(id);
        }
    }

    private void receiveData(int id) throws CapteurInknowException, RemoteException{
        executor = Executors.newSingleThreadScheduledExecutor();
        if(this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()){
            CapteurInterface capteur = this.capteurs.get(id);
            Runnable task = () -> {
                while (true) {
                    try {
                        LocalDateTime now = LocalDateTime.now();
                        String formatter = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                        //ecrire dans un fichier
                        FileWriter writer = new FileWriter("Data/data.txt", true);
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
        if (!this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()) {
            return this.capteurs.get(id);
        } else {
            return null;
        }
    }
}
