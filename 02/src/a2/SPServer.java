package a2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class SPServer extends UnicastRemoteObject implements Publisher {

  private LinkedList<Subscriber> subscribers = new LinkedList<>();

  protected SPServer() throws RemoteException {
  }

  public static void main(String[] args) {
    try {
      java.rmi.registry.LocateRegistry.createRegistry(1099);
      SPServer server = new SPServer();
      Naming.bind("rmi://localhost:1099/server", server);
    } catch (RemoteException | AlreadyBoundException | MalformedURLException re) {
      re.printStackTrace();
    }
  }

  private void notifyAllSubs(String message) {
    for (Subscriber subscriber : subscribers) {
      try {
        subscriber.notifySub(message);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void publish(Subscriber subscriber, String message) throws RemoteException {
    System.out.println("publishing \"" + message + "\" from " + subscriber.getName());
    notifyAllSubs(subscriber.getName() + " said: " + message);
  }

  @Override
  public void subscribe(Subscriber subscriber) throws RemoteException {
    System.out.println("adding " + subscriber.getName() + " to subscribers");
    subscribers.add(subscriber);
  }

  @Override
  public void unsubscribe(Subscriber subscriber) throws RemoteException {
    System.out.println("removing " + subscriber.getName() + " from subscribers");
    subscribers.remove(subscriber);
  }
}
