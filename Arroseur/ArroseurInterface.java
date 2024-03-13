package Arroseur;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ArroseurInterface extends Remote {
    void activer() throws RemoteException;
    void desactiver() throws RemoteException;
    double getLatitude()throws RemoteException;
    double getLongitude()throws RemoteException;
    int getId()throws RemoteException;
    boolean getEstActif()throws RemoteException;
    double getSeuilTemp()throws RemoteException;
    double getSeuilHumi()throws RemoteException;
}