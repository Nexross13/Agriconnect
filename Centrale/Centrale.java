package Centrale;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;

public interface Centrale extends java.rmi.Remote {
    public void registerCapteur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException;
    public void afficherData(int id) throws RemoteException;
    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException;
    public HashMap<Integer, CapteurInterface> getCapteurs() throws RemoteException;
}
