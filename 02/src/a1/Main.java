package a1;

public class Main {

  public static void main(String[] args) {
    ServerKVStore server = new ServerKVStore();
    new Thread(server).start();
    ClientKVStore client = new ClientKVStore();
    new Thread(client).start();
    client.put("one", "oneValue");
    System.out.println(client.get("one"));
  }
}
