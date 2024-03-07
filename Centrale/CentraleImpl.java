package Centrale;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Capteur.CapteurInknowException;

public class CentraleImpl extends UnicastRemoteObject implements Centrale{
    
    private CentraleMetier centraleMetier;

    public CentraleImpl(CentraleMetier centraleMetier) throws RemoteException {
        super();
        this.centraleMetier = new CentraleMetier();
    }

    @Override
    public void registerCapteur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException{
        centraleMetier.registerCapteur(id);
    }

    @Override
    public void afficherData(int id) throws RemoteException {
        centraleMetier.afficherData(id);
    }

    @Override
    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException {
        centraleMetier.modifInterval(id, intervalle);
    }
}
