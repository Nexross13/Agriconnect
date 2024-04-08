package Client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote{
    void receiveNotification(String message) throws RemoteException;
    int getId() throws RemoteException;
    void demarrage() throws RemoteException;
    void connecter() throws RemoteException, MalformedURLException, NotBoundException, IOException;
}
