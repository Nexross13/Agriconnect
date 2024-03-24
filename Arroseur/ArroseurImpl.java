package Arroseur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Capteur.CapteurInterface;

// Classe CapteurImpl qui implémente l'interface CapteurInterface pour la communication RMI.
public class ArroseurImpl extends UnicastRemoteObject implements ArroseurInterface {
    // Identifiant unique du capteur.
    private int id;
    // Coordonnées géographiques du capteur.
    private double latitude;
    private double longitude;
    private String zone;
    private double seuilTemp;
    private double seuilHumi;
    private boolean estActif;

    // Constructeur du capteur implémentant les propriétés nécessaires.
    public ArroseurImpl(int id) throws RemoteException {
        super();
        this.id = id;
        this.seuilTemp = 30.0;
        this.seuilHumi = 35.0;
        this.estActif = false;
    }

    // Getters pour les propriétés du capteur.
    public int getId() throws RemoteException{
        return id;
    }

    public double getLatitude() throws RemoteException{
        return latitude;
    }

    public double getLongitude() throws RemoteException{
        return longitude;
    }

    public double getSeuilTemp() throws RemoteException{
        return seuilTemp;
    }

    public double getSeuilHumi() throws RemoteException{
        return seuilHumi;
    }

    public boolean getEstActif() throws RemoteException{
        return estActif;
    }

    public String getZone() throws RemoteException{
        return zone;
    }

    // Setters pour les propriétés du capteur.
    public void setLatitude(double latitude) throws RemoteException{
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) throws RemoteException{
        this.longitude = longitude;
    }
    
    public void setSeuilTemp(double seuilTemp) throws RemoteException{
        this.seuilTemp = seuilTemp;
    }

    public void setSeuilHumi(double seuilHumi) throws RemoteException{
        this.seuilHumi = seuilHumi;
    }

    public void setZone(String zone) throws RemoteException{
        this.zone = zone;
    }

    // Active le capteur, l'ajoute à la centrale et commence l'envoi périodique des mesures.
    @Override
    public void activer() throws RemoteException {
        if (!estActif) {
            estActif = true;
        }
    }

    // Désactive le capteur en arrêtant le timer.
    @Override
    public void desactiver() throws RemoteException {
        if (estActif) {
            estActif = false;
        }
    }

    // Arrose la plante.
    @Override
    public void arroser(CapteurInterface capteur) throws RemoteException {
        System.out.println("Arrosage de la plante...");
        // Crée un ScheduledExecutorService avec 1 thread.
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        
        // Crée une tâche qui incrémente la variable i toutes les 10 secondes.
         // Utilise un tableau pour permettre la modification dans l'expression lambda
        Runnable task = () -> {
            try {
                capteur.setHumidite(1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };

        // Planifie la tâche pour s'exécuter toutes les 10 secondes
        executor.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);

        // Crée une autre tâche pour stopper l'executor après 2 minutes.
        executor.schedule(() -> {
            executor.shutdown();
            try {
                // Attend la terminaison de toutes les tâches planifiées
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Annule toutes les tâches en cours si elles ne terminent pas
                }
            } catch (InterruptedException ex) {
                executor.shutdownNow();
                Thread.currentThread().interrupt(); // Rétablit le statut d'interruption
            }
            System.out.println("Fin de l'exécution après 2 minutes.");
        }, 2, TimeUnit.MINUTES);
    }
}