package Arroseur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
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
        // generate random latitude entre 44.66307858413157 et 44.73046542148722
        this.latitude = 44.66307858413157 + Math.random() * (44.73046542148722 - 44.66307858413157);
        // generate random longitude entre -0.4345503688752195 et -0.4751061712705495
        this.longitude = -0.4345503688752195 + Math.random() * (-0.4751061712705495 - -0.4345503688752195);
        this.seuilTemp = 40.0;
        this.seuilHumi = 35.0;
        this.estActif = false;
    }

    // Getters pour les propriétés de l'arroseur.
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

    // Setters pour les propriétés de l'arroseur.
    
    public void setSeuilTemp(double seuilTemp) throws RemoteException{
        this.seuilTemp = seuilTemp;
    }

    public void setSeuilHumi(double seuilHumi) throws RemoteException{
        this.seuilHumi = seuilHumi;
    }

    public void setZone(String zone) throws RemoteException{
        this.zone = zone;
    }

    // Active l'arroseur, l'ajoute à la centrale et commence l'envoi périodique des mesures.
    @Override
    public void activer(HashMap<Integer, CapteurInterface> capteurs) throws RemoteException {
        if (!estActif) {
            estActif = true;
            arroser(capteurs);
        }
    }

    // Désactive l'arroseur en arrêtant le timer.
    @Override
    public void desactiver() throws RemoteException {
        if (estActif) {
            estActif = false;
        }
    }

    // Arrose la plante.
    private void arroser(HashMap<Integer, CapteurInterface> capteurs) throws RemoteException {
        // Crée un ScheduledExecutorService avec 1 thread.
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        
        // Crée une tâche qui incrémente la variable i toutes les 10 secondes.
         // Utilise un tableau pour permettre la modification dans l'expression lambda
        for (CapteurInterface capteur : capteurs.values()) {
            if (capteur.getZone() != this.zone || capteur.getEstActif() == false){
                break;
            }

            Runnable task = () -> {
                try {
                    capteur.setHumidite(1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            };

            // Planifie la tâche pour s'exécuter toutes les 10 secondes
            executor.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
        }

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