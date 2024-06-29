package com.game.caches.shaders;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public abstract class AbstractShaderCache {
  protected final int programId;
  protected final HashMap<String, Integer> cache;
  protected final List<String> missingKeys;

  public AbstractShaderCache(int programId) {
    this.programId = programId;

    cache = new HashMap<>();
    missingKeys = new ArrayList<>();
  }

  public int get(String key) {
    int location = glLocation(key);
    if (location >= 0) cache.putIfAbsent(key, location);
    return location;
  }

  public boolean has(String key) {
    return cache.containsKey(key) || glLocation(key) >= 0;
  }

  protected abstract int glLocation(String key);

  protected int check(int location, String type, String value) {
    if (location < 0 && !missingKeys.contains(value)) {
      String errorMessage = String.format(
        "ERROR: Shader %s {%s} location [%d] could not be located in shader program %s because it does not exist or is unused!",
        type,
        value,
        location,
        programId
      );
      log.debug(errorMessage);
      missingKeys.add(value);
    } else if (location >= 0) missingKeys.remove(value);
    return location;
  }
}
