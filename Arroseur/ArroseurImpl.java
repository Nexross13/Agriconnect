package Arroseur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// Classe CapteurImpl qui implémente l'interface CapteurInterface pour la communication RMI.
public class ArroseurImpl extends UnicastRemoteObject implements ArroseurInterface {
    // Identifiant unique du capteur.
    private int id;
    // Coordonnées géographiques du capteur.
    private double latitude;
    private double longitude;
    private double seuilTemp;
    private double seuilHumi;
    private boolean estActif;

    // Constructeur du capteur implémentant les propriétés nécessaires.
    public ArroseurImpl(int id, double latitude, double longitude) throws RemoteException {
        super();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.seuilTemp = 30.0;
        this.seuilHumi = 35.0;
        this.estActif = true;
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
    
    public void setSeuilTemp(double seuilTemp) throws RemoteException{
        this.seuilTemp = seuilTemp;
    }

    public void setSeuilHumi(double seuilHumi) throws RemoteException{
        this.seuilHumi = seuilHumi;
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
}
