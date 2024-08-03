package com.game.engine.settings;

import com.game.utils.enums.EFont;
import com.game.utils.enums.ELogic;
import org.joml.Vector3f;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.IntStream;

public class INIModelSection {
  protected final Map<String, String> section;

  public INIModelSection(Map<String, String> section) {
    this.section = section;
  }

  public int getInt(String key) {
    return Integer.parseInt(get(key));
  }

  public float getFloat(String key) {
    return Float.parseFloat(get(key));
  }

  public boolean getBool(String key) {
    return Boolean.parseBoolean(get(key));
  }

  public EFont getEFont(String key) {
    return Enum.valueOf(EFont.class, get(key));
  }

  public String getELogic(String key) {
    String value = get(key);
    // TODO: Implement a better strategy for loading logic based on settings. As it stands this enumSet stream circumvents the IllegalArgumentException that gets thrown by Enum.valueOf
    return EnumSet.allOf(ELogic.class).stream().anyMatch(en -> en.name().equals(value)) ? Enum
      .valueOf(ELogic.class, value)
      .value() : value;
  }

  public Vector3f getVector3f(String key) {
    float[] values = getFloatArr(key);
    float x = values[0];
    float y = values[1];
    float z = values[2];

    return new Vector3f(x, y, z);
  }

  public float[] getFloatArr(String key) {
    String[] values = getArr(key);
    int l = values.length;
    float[] results = new float[l];

    IntStream.range(0, l).forEach(i -> results[i] = Float.parseFloat(values[i]));

    return results;
  }

  public String[] getArr(String key) {
    return get(key).split(",");
  }

  public String get(String key) {
    return section.get(key);
  }
}
