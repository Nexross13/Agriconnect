package Capteur;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// Classe CapteurImpl qui implémente l'interface CapteurInterface pour la communication RMI.
public class CapteurImpl extends UnicastRemoteObject implements CapteurInterface {
    // Identifiant unique du capteur.
    private int id;
    // Coordonnées géographiques du capteur.
    private double latitude;
    private double longitude;
    private int intervalle = 5;
    // Dernières mesures de température et d'humidité, initialisées avec des valeurs aléatoires.
    private double temperature = Math.round(Math.random() * 30 * 10) / 10.0;
    private double humidite = Math.round(Math.random() * 100 * 10) / 10.0;
    private boolean estActif;

    // Constructeur du capteur implémentant les propriétés nécessaires.
    public CapteurImpl(int id, double latitude, double longitude) throws RemoteException {
        super();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getTemperature() throws RemoteException{
        makeMeasureTemp();
        return temperature;
    }

    public double getHumidite() throws RemoteException{
        makeMeasureHumi();
        return humidite;
    }

    public int getInterval() throws RemoteException{
        return intervalle;
    }

    public void setInterval(int intervalle) throws RemoteException{
        this.intervalle = intervalle;
    }

    private void makeMeasureTemp() {
        temperature = Math.round(Math.random() * 30 * 10) / 10.0;
    }

    private void makeMeasureHumi() {
        humidite = Math.round(Math.random() * 100 * 10) / 10.0;
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
