public class Main {

  public static void main(String[] args) {
    MiningClient client = new MiningClient();
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
