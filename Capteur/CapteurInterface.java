package Capteur;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CapteurInterface extends Remote {
    void envoyerDonnees(double temperature, double humidite) throws RemoteException;
    void activer() throws RemoteException;
    void desactiver() throws RemoteException;
    void setIntervalleMesure(long nouvelIntervalle) throws RemoteException;
}
