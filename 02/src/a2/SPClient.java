package a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SPClient extends UnicastRemoteObject implements Subscriber {

  private Publisher observable;
  private String name;

  public SPClient(String name)
      throws RemoteException, MalformedURLException, NotBoundException {
    this.name = name;
    observable = (Publisher) Naming.lookup("server");
    observable.subscribe(this);
    System.out.println("subscribing to " + observable);
  }

  public static void main(String[] args) {
    try {
      SPClient client = new SPClient(args[0]);
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String line = in.readLine();
      while (!line.equals("/quit")) {
        client.publish(line);
        line = in.readLine();
      }
      client.unsubscribe();
      System.exit(0);
    } catch (NotBoundException | IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getName() throws RemoteException {
    return this.name;
  }

  public void publish(String msg) throws RemoteException {
    System.out.println("publishing \"" + msg + "\"");
    observable.publish(this, msg);
  }

  public void unsubscribe() throws RemoteException {
    observable.unsubscribe(this);
  }

  @Override
  public void notifySub(String subject) throws RemoteException {
    System.out.println("received: " + subject);
  }

}
