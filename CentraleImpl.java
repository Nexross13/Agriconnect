import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class CentraleImpl extends UnicastRemoteObject implements Centrale{
    
    private CentraleMetier centraleMetier;

    public CentraleImpl(CentraleMetier centraleMetier) throws RemoteException {
        super();
        this.centraleMetier = new CentraleMetier();
    }
    
    @Override
    public HashMap receiveData(String name) throws RemoteException, CapteurInknowException{
        return centraleMetier.receiveData(name);
    }

    @Override
    public void registerCapteur(String name, double latitude, double longitude) throws RemoteException{
        centraleMetier.registerCapteur(name, latitude, longitude);
    }

    @Override
    public void unregisterCapteur(String name) throws RemoteException, CapteurInknowException{
        centraleMetier.unregisterCapteur(name);
    }
}
