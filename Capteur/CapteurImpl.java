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
    private String zone;
    private int intervalle = 5;
    // Dernières mesures de température et d'humidité, initialisées avec des valeurs aléatoires.
    private double temperature = 25 + Math.round(Math.random() * 15 * 10) / 10.0; 
    private double humidite = 10;
    //private double humidite = Math.round(Math.random() * 100 * 10) / 10.0; // 0-100
    private boolean estActif;

    // Constructeur du capteur implémentant les propriétés nécessaires.
    public CapteurImpl(int id) throws RemoteException {
        super();
        this.id = id;
        this.estActif = true;
        // generate random latitude entre 44.66307858413157 et 44.73046542148722
        this.latitude = 44.66307858413157 + Math.random() * (44.73046542148722 - 44.66307858413157);
        // generate random longitude entre -0.4345503688752195 et -0.4751061712705495
        this.longitude = -0.4345503688752195 + Math.random() * (-0.4751061712705495 - -0.4345503688752195);
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

    public boolean getEstActif() throws RemoteException{
        return estActif;
    }

    public int getInterval() throws RemoteException{
        return intervalle;
    }

    public String getZone() {
        return zone;
    }

    // Setters pour les propriétés du capteur.

    public void setHumidite(int ajout) throws RemoteException{
        this.humidite += ajout;
    }

    public void setInterval(int intervalle) throws RemoteException{
        this.intervalle = intervalle;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    private void makeMeasureTemp() {
        temperature += (Math.random() * 6 - 3);
        temperature = Math.min(Math.max(temperature, 0), 30);
        temperature = Math.round(temperature * 100) / 100.0;
    }

    private void makeMeasureHumi() {
        humidite += (Math.random() * 6 - 3);
        humidite = Math.min(Math.max(humidite, 0), 100);
        humidite = Math.round(humidite * 100) / 100.0;
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
