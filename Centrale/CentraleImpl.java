package Centrale;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;

import Arroseur.ArroseurInterface;
import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;
import Client.ClientInterface;

public class CentraleImpl extends UnicastRemoteObject implements Centrale{
    
    private CentraleMetier centraleMetier;

    public CentraleImpl(CentraleMetier centraleMetier) throws RemoteException {
        super();
        this.centraleMetier = new CentraleMetier();
    }

    @Override
    public void registerCapteur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException, SQLException{
        centraleMetier.registerCapteur(id);
    }

    @Override
    public void registerArroseur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException, SQLException{
        centraleMetier.registerArroseur(id);
    }

    @Override
    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.modifInterval(id, intervalle);
    }

    @Override
    public HashMap<Integer, CapteurInterface> getCapteurs() throws RemoteException {
        return centraleMetier.getCapteurs();
    }

    @Override
    public HashMap<Integer, ArroseurInterface> getArroseurs() throws RemoteException {
        return centraleMetier.getArroseurs();
    }

    @Override
    public CapteurInterface getLastInfoCapteur(int id) throws RemoteException {
        return centraleMetier.getLastInfoCapteur(id);
    }

    @Override
    public void activerCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.activerCapteur(id);
    }

    @Override
    public void activerArroseur(int id) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.activerCapteur(id);
    }

    @Override
    public void desactiverCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.desactiverCapteur(id);
    }

    @Override
    public void desactiverArroseur(int id) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.desactiverCapteur(id);
    }

    @Override
    public HashMap<String, Object> getMoyenne(int id) throws RemoteException, SQLException {
        return centraleMetier.getMoyenne(id);
    }

    @Override
    public void addClient(ClientInterface client) throws RemoteException, IOException {
        centraleMetier.addClient(client);
    }
}
