package Centrale;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;

import Arroseur.ArroseurInterface;
import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;
import Client.ClientInterface;

public interface Centrale extends java.rmi.Remote {
    public void registerCapteur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException, SQLException;
    public void registerArroseur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException, SQLException;
    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException, IOException;
    public HashMap<Integer, CapteurInterface> getCapteurs() throws RemoteException;
    public HashMap<Integer, ArroseurInterface> getArroseurs() throws RemoteException;
    public CapteurInterface getLastInfoCapteur(int id) throws RemoteException;
    public void activerCapteur(int id) throws RemoteException, CapteurInknowException, IOException;
    public void activerArroseur(int id) throws RemoteException, CapteurInknowException, IOException;
    public void desactiverCapteur(int id) throws RemoteException, CapteurInknowException, IOException;
    public void desactiverArroseur(int id) throws RemoteException, CapteurInknowException, IOException;
    public HashMap<String, Object> getMoyenne(int id) throws RemoteException, SQLException;
    public void addClient(ClientInterface client) throws RemoteException, IOException;
}
