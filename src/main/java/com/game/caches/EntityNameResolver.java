package com.game.caches;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EntityNameResolver {
  private static final Pattern ENTITY_NAME_APPENDIX = Pattern.compile(".*(\\([0-9]+\\))$");
  private final HashSet<String> entityNames;

  public EntityNameResolver() {
    entityNames = new HashSet<>();
  }

  public String getAvailable(String name) {
    String entityName = hasMatchingName(name) ? createFromAvailable(name) : name;
    entityNames.add(entityName);
    return entityName;
  }

  public void removeName(String name) {
    entityNames.remove(name);
  }

  public boolean hasMatchingName(String name) {
    return entityNames.contains(name) || matchingNames(name).findAny().isPresent();
  }

  public Stream<String> matchingNames(String name) {
    return entityNames.stream().filter((s) -> s.startsWith(name));
  }

  int appendixValue(String name) {
    Matcher m = ENTITY_NAME_APPENDIX.matcher(name);
    if (m.matches()) {
      String g = m.group(0);
      if (!StringUtils.isEmpty(g)) {
        g = g.replace("(", "").replace(")", "");
        return Integer.parseInt(g);
      }
    }
    return -1;
  }

  IntStream matchingNameAppendices(String name) {
    return matchingNames(name).mapToInt(this::appendixValue).filter(a -> a >= 0);
  }

  int maxNameAppendixValue(String name) {
    return matchingNameAppendices(name).max().orElse(0);
  }

  String createFromAvailable(String name) {
    int appendix = maxNameAppendixValue(name);
    return name.concat("(%d)".formatted(appendix + 1));
  }
}
