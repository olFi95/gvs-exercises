import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class ZMQRequest {

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://gvs.lxd-vs.uni-ulm.de:27347");
            socket.send("Hello".getBytes());
            byte[] reply = socket.recv(0);
            System.out.println(new String(reply));
        }
    }

}
