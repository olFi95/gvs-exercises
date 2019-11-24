package a2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Subscriber extends Remote {

  void notifySub(String subject) throws RemoteException;

  String getName() throws RemoteException;
}
