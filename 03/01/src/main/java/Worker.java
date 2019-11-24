import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class Worker implements Runnable {
  String challenge;
  MiningClient mc;

  public Worker(String challenge, MiningClient mc) {
    this.challenge = challenge;
    this.mc = mc;
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      String attempt = mc.getAttempt();
      String hash = Hashing.sha256().hashString(attempt, StandardCharsets.UTF_8).toString();
      if (hash.startsWith(this.challenge)) {
        System.out.println("challenge: " + challenge + ": found solution: " + attempt + " + with hash: " + hash);
        mc.submitSolution(attempt);
      }
    }
  }
}
