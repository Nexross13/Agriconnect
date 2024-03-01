package Capteur;


import java.io.Serializable;
import java.time.LocalDateTime;

public class CapteurInfo implements Serializable {
    private int id;
    private double derniereTemperature;
    private double derniereHumidite;
    private double latitude;
    private double longitude;
    private boolean estActif; 
    private LocalDateTime timestamp; 

    public CapteurInfo(int id, double derniereTemperature, double derniereHumidite, double latitude, double longitude, boolean estActif, LocalDateTime timestamp) {
        this.id = id;
        this.derniereTemperature = derniereTemperature;
        this.derniereHumidite = derniereHumidite;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estActif = estActif;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public double getDerniereTemperature() {
        return derniereTemperature;
    }

    public double getDerniereHumidite() {
        return derniereHumidite;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean EstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;        
    }
    
}


