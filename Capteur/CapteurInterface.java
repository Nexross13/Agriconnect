package Capteur;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CapteurInterface extends Remote {
    void activer() throws RemoteException;
    void desactiver() throws RemoteException;
    double getLatitude()throws RemoteException;
    double getLongitude()throws RemoteException;
    int getId()throws RemoteException;
    double getTemperature()throws RemoteException;
    double getHumidite()throws RemoteException;
    int getInterval()throws RemoteException;
    void setInterval(int intervalle)throws RemoteException;
    boolean getEstActif()throws RemoteException;
    void setHumidite(int ajout) throws RemoteException;
    void setLatitude(double latitude) throws RemoteException;
    void setLongitude(double longitude) throws RemoteException;
    void setZone(String terrain) throws RemoteException;
    String getZone() throws RemoteException;
}
