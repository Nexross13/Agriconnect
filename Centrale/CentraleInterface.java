package Centrale;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

import Capteur.CapteurInfo;
import Capteur.CapteurInterface;

public interface CentraleInterface extends Remote {
    void ajouterCapteur(int id, CapteurInterface capteur, double latitude, double longitude) throws RemoteException;
    void retirerCapteur(int id) throws RemoteException;
    void recevoirDonnees(int id, double temperature, double humidite) throws RemoteException;
    List<CapteurInfo> listerCapteurs() throws RemoteException; 
    void modifierIntervalleMesure(int capteurId, long nouvelIntervalle) throws RemoteException;
    CapteurInfo obtenirInfosCapteur(int id) throws RemoteException; 
    void activerCapteur(int id) throws RemoteException; 
    double[] calculerMoyenne(int idCapteur, LocalDateTime debut, LocalDateTime fin) throws java.rmi.RemoteException;
    String[] determinerTendance(int idCapteur, LocalDateTime debut, LocalDateTime fin) throws java.rmi.RemoteException;
}


