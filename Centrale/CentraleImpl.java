package Centrale;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;

public class CentraleImpl extends UnicastRemoteObject implements Centrale{
    
    private CentraleMetier centraleMetier;

    public CentraleImpl(CentraleMetier centraleMetier) throws RemoteException {
        super();
        this.centraleMetier = new CentraleMetier();
    }

    @Override
    public void registerCapteur(int id) throws RemoteException, MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException{
        centraleMetier.registerCapteur(id);
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
    public CapteurInterface getLastInfoCapteur(int id) throws RemoteException {
        return centraleMetier.getLastInfoCapteur(id);
    }

    @Override
    public void activerCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.activerCapteur(id);
    }

    @Override
    public void desactiverCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        centraleMetier.desactiverCapteur(id);
    }
}
