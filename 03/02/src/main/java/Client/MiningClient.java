package Client;

import static zmq.ZMQ.ZMQ_POLLIN;
import static zmq.ZMQ.ZMQ_POLLOUT;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;


public class MiningClient {
  private ZContext context;
  ZMQ.Socket subSocket;
  ZMQ.Socket reqSocket;
  List<Thread> workerThreads;
  String challenge;
  AtomicLong attemptNum;
  int availProc;

  public static void main(String[] args) {
    new MiningClient();
    while (true) ;
  }

  public MiningClient() {
    init();
  }

  public void init() {
    this.context = new ZContext();
    subSocket = context.createSocket(SocketType.SUB);
    subSocket.connect("tcp://gvs.lxd-vs.uni-ulm.de:27341");
    subSocket.subscribe("");
    reqSocket = context.createSocket(SocketType.REQ);
    reqSocket.connect("tcp://gvs.lxd-vs.uni-ulm.de:27349");
    System.out.println("connections established");

    this.workerThreads = new ArrayList<>();
    this.challenge = "";
    this.attemptNum = new AtomicLong(0);
    this.availProc = Runtime.getRuntime().availableProcessors();
    //3
    setUpNewChallenge();
  }

  public void setUpNewChallenge() {
    this.challenge = new String(subSocket.recv(), ZMQ.CHARSET);
    System.out.println("received " + this.challenge);
    this.attemptNum.set(0);

    for (int i = 0; i < (availProc - 1); i++) {
      Thread worker = new Thread(new Worker(challenge, this));
      this.workerThreads.add(worker);
      worker.start();
    }
  }

  public void submitSolution(String sol) {
    for (Thread t : workerThreads) {
      t.interrupt();
    }
    workerThreads.clear();
    while (!(reqSocket.getEvents() == ZMQ_POLLOUT)) ;
    System.out.println("server is ready for solution");
    reqSocket.send(sol.getBytes(ZMQ.CHARSET));
    while (!(reqSocket.getEvents() == ZMQ_POLLIN)) ;
    System.out.println("sent solution");
    System.out.println(new String(reqSocket.recv(), ZMQ.CHARSET));

    setUpNewChallenge();
  }

  public String getAttempt() {
    return this.challenge + Long.toHexString(this.attemptNum.getAndIncrement());
  }
}
