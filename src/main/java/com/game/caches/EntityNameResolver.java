package com.game.caches;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityNameResolver {
  private final HashSet<String> entityNames;

  private static final Pattern ENTITY_NAME_APPENDIX = Pattern.compile(".*(\\([0-9]*\\))$");

  public EntityNameResolver() {
    entityNames = new HashSet<>();
  }

  public String getAvailable(String name) {
    String entityName = entityNames.contains(name) ? createFromAvailable(name) : name;
    entityNames.add(entityName);
    return entityName;
  }

  public void removeName(String name) {
    entityNames.remove(name);
  }

  String createFromAvailable(String name) {
    Matcher m = ENTITY_NAME_APPENDIX.matcher(name);
    int appendix = m.matches() ? entityNames
      .stream()
      .filter((s) -> s.startsWith(name))
      .mapToInt(s -> {
        String g = m.group(0);
        return Integer.parseInt(g);
      })
      .max()
      .orElse(0) : 0;

    return name.concat("(%d)".formatted(appendix + 1));
  }
}
