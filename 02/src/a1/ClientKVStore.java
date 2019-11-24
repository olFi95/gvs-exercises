package a1;

import java.util.concurrent.LinkedBlockingQueue;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ClientKVStore implements KVStore, Runnable {

  ZContext context = new ZContext();
  private ZMQ.Socket socket;
  private LinkedBlockingQueue<String> inQueue = new LinkedBlockingQueue<>();
  private LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<>();

  @Override
  public void put(String key, String value) {
    try {
      inQueue.put("put," + key + "," + value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String get(String key) {
    try {
      inQueue.put(("get," + key));
      return outQueue.take();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean isEmpty() {
    try {
      inQueue.put("isEmpty");
      return outQueue.take().equals("true");
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }

  @Override
  public void run() {
    try {
      System.out.println("starting client");
      socket = context.createSocket(SocketType.REQ);
      socket.connect("tcp://localhost:5555");
      while (!Thread.currentThread().isInterrupted()) {
        socket.send(inQueue.take().getBytes(ZMQ.CHARSET));
        byte[] reply = socket.recv(0);
        outQueue.put(new String(reply, ZMQ.CHARSET));
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
