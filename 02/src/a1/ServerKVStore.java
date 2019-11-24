package a1;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ServerKVStore implements Runnable {

  private ActualKVStore backend = new ActualKVStore();

  @Override
  public void run() {
    try (ZContext context = new ZContext()) {
      // Socket to talk to clients
      System.out.println("starting server");
      ZMQ.Socket socket = context.createSocket(SocketType.REP);
      socket.bind("tcp://localhost:5555");

      while (!Thread.currentThread().isInterrupted()) {
        byte[] reply = socket.recv(0);
        String request = new String(reply, ZMQ.CHARSET);
        String[] requestSplit = request.split(",");
        String response = "";
        switch (requestSplit[0]) {
          case "get":
            response = backend.get(requestSplit[1]);
            if (response == null) {
              response = "not ok";
            }
            break;
          case "put":
            backend.put(requestSplit[1], requestSplit[2]);
            response = "ok";
            break;
          case "isEmpty":
            response = String.valueOf(backend.isEmpty());
            break;
          default:
            response = "wat";
        }
        socket.send(response.getBytes(ZMQ.CHARSET));
      }
    }
  }
}
