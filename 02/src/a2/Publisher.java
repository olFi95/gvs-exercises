package a2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Publisher extends Remote {

  void publish(Subscriber subscriber, String message) throws RemoteException;

  void subscribe(Subscriber subscriber) throws RemoteException;

  void unsubscribe(Subscriber subscriber) throws RemoteException;
}
