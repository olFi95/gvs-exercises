package a1;

import java.util.HashMap;

public class ActualKVStore implements KVStore {

  private HashMap<String, String> internalKVStore = new HashMap<>();

  @Override
  public void put(String key, String value) {
    internalKVStore.put(key, value);
  }

  @Override
  public String get(String key) {
    return internalKVStore.get(key);
  }

  @Override
  public boolean isEmpty() {
    return internalKVStore.isEmpty();
  }
}
