import java.util.HashMap;

public interface Centrale extends java.rmi.Remote {
    public HashMap receiveData(String name) throws java.rmi.RemoteException, CapteurInknowException;
    public void registerCapteur(String name, double latitude, double longitude) throws java.rmi.RemoteException;
    public void unregisterCapteur(String name) throws java.rmi.RemoteException, CapteurInknowException;
}
