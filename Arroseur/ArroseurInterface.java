package Arroseur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Capteur.CapteurInterface;

public interface ArroseurInterface extends Remote {
    void activer() throws RemoteException;
    void desactiver() throws RemoteException;
    double getLatitude()throws RemoteException;
    double getLongitude()throws RemoteException;
    int getId()throws RemoteException;
    boolean getEstActif()throws RemoteException;
    double getSeuilTemp()throws RemoteException;
    double getSeuilHumi()throws RemoteException;
    void arroser(CapteurInterface capteur) throws RemoteException;
    void setLatitude(double latitude) throws RemoteException;
    void setLongitude(double longitude) throws RemoteException;
    void setZone(String terrain) throws RemoteException;
    String getZone() throws RemoteException;
}