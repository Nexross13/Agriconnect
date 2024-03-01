package Capteur;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

import Centrale.CentraleInterface;

// Classe CapteurImpl qui implémente l'interface CapteurInterface pour la communication RMI.
public class CapteurImpl extends UnicastRemoteObject implements CapteurInterface {
    // Identifiant unique du capteur.
    private int id;
    // Coordonnées géographiques du capteur.
    private double latitude;
    private double longitude;
    // Dernières mesures de température et d'humidité, initialisées avec des valeurs aléatoires.
    private double temperature = Math.round(Math.random() * 30 * 10) / 10.0;
    private double humidite = Math.round(Math.random() * 100 * 10) / 10.0;
    // Référence à la centrale pour communiquer les mesures.
    private CentraleInterface centrale;
    // Timer pour l'envoi périodique des mesures.
    private Timer timer;
    // Intervalle entre les envois des mesures en millisecondes, 5 secondes par défaut.
    private long intervalleMesure = 5000;
    // Flag pour suivre si le capteur est actif ou non.
    private boolean estActif;

    // Constructeur du capteur implémentant les propriétés nécessaires.
    public CapteurImpl(int id, double latitude, double longitude, CentraleInterface centrale) throws RemoteException {
        super();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.centrale = centrale;
        this.timer = new Timer(true);
        this.estActif = false;
    }

    // Active le capteur, l'ajoute à la centrale et commence l'envoi périodique des mesures.
    @Override
    public void activer() throws RemoteException {
        if (!estActif) {
            estActif = true;
            centrale.ajouterCapteur(id, this, latitude, longitude);
            programmerEnvoiDonnees();
        }
    }

    // Désactive le capteur en arrêtant le timer.
    @Override
    public void desactiver() throws RemoteException {
        if (estActif) {
            estActif = false;
            timer.cancel();
        }
    }

    // Envoie les données de température et d'humidité à la centrale si le capteur est actif.
    @Override
    public void envoyerDonnees(double temperature, double humidite) throws RemoteException {
        if (estActif) {
            centrale.recevoirDonnees(id, temperature, humidite);
        }
    }

    // Modifie l'intervalle d'envoi des mesures et reprogramme le timer si le capteur est actif.
    public void setIntervalleMesure(long nouvelIntervalle) {
        this.intervalleMesure = nouvelIntervalle * 1000;
        if (estActif) {
            programmerEnvoiDonnees();
        }
    }

    // Configure le timer pour envoyer périodiquement les mesures à la centrale.
    private void programmerEnvoiDonnees() {
        timer.cancel();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (estActif) {
                    try {
                        // Ajuste la température et l'humidité dans une plage réaliste
                        temperature += (Math.random() * 6 - 3);
                        humidite += (Math.random() * 6 - 3);

                        // Assure que les valeurs restent dans des limites logiques
                        temperature = Math.min(Math.max(temperature, 0), 30);
                        humidite = Math.min(Math.max(humidite, 0), 100);
                        envoyerDonnees(temperature, humidite);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, intervalleMesure);
    }
}
