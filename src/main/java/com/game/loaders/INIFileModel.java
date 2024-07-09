package com.game.loaders;

import com.game.utils.application.LoaderUtils;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Accessors(fluent = true)
@Slf4j
public class INIFileModel {
  private final Map<String, Map<String, String>> contents;

  INIFileModel() {
    contents = new HashMap<>();
  }

  public boolean isEmpty() {
    return contents.isEmpty() || contents.values().stream().allMatch(Map::isEmpty);
  }

  public Map<String, String> section(String section) {
    return contents.computeIfAbsent(section, s -> new HashMap<>());
  }

  public void addData(String section, String subSection, String data) {
    section(section).put(subSection, data);
  }

  public void readConfig(INIConfiguration config) {
    config.getSections().forEach(section -> {
      SubnodeConfiguration configSection = config.getSection(section);
      Iterator<String> keys = configSection.getKeys();
      while (keys.hasNext()) {
        String key = keys.next();
        String value = configSection.getProperty(key).toString();
        addData(section, key, value);
      }
    });
  }

  public void clear() {
    contents.clear();
  }

  public void clearSection(String section) {
    section(section).clear();
  }

  public static INIFileModel load(String path) {
    INIConfiguration config = new INIConfiguration();
    INIFileModel model = new INIFileModel();
    String fullPath = LoaderUtils.sanitizeFilePath(path);
    try (FileReader in = new FileReader(fullPath)) {
      config.read(in);
      model.readConfig(config);
    } catch (ConfigurationException | IOException ex) {
      log.error("Could not load INI file {}", fullPath);
      throw new RuntimeException(ex);
    }
    return model;
  }
}
