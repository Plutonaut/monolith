package com.game.utils.application.values;

import com.game.utils.enums.EFont;
import com.game.utils.enums.ELogic;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ValueMap {
  protected final Map<String, String> section;

  // TODO: Revisit
  public ValueMap() { this(new HashMap<>()); }

  public ValueMap(Map<String, String> section) {
    this.section = new HashMap<>();
    copy(section);
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

  public Vector2f getVector2f(String key) {
    float[] values = getFloatArr(key);
    float x = values[0];
    float y = values[1];

    return new Vector2f(x, y);
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

  public Color getColor(String key) {
    int[] values = getIntArr(key);
    int r = values[0];
    int g = values.length > 1 ? values[1] : r;
    int b = values.length > 2 ? values[2] : g;
    int a = values.length > 3 ? values[3] : b;

    return new Color(r, g, b, a);
  }

  public int[] getIntArr(String key) {
    String[] values = getArr(key);
    int l = values.length;
    int[] results = new int[l];
    IntStream.range(0, l).forEach(i -> results[i] = Integer.parseInt(values[i]));
    return results;
  }

  public String[] getArr(String key) {
    return get(key).split(",");
  }

  public String get(String key) {
    return section.getOrDefault(key, "");
  }

  public void setBool(String key, boolean value) {
    section.put(key, Boolean.toString(value));
  }

  public void setInt(String key, int value) {
    section.put(key, Integer.toString(value));
  }

  public void setFloat(String key, float value) {
    section.put(key, Float.toString(value));
  }

  public void setVector3f(String key, Vector3f value) {
    section.put(key, fromFloatArr(value.x(), value.y(), value.z()));
  }

  public void setVector2f(String key, Vector2f value) {
    section.put(key, fromFloatArr(value.x(), value.y()));
  }

  public void setColor(String key, Color value) {
    section.put(
      key,
      fromIntArr(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha())
    );
  }

  public String fromIntArr(int... values) {
    String[] results = new String[values.length];
    for (int i = 0; i < values.length; i++)
      results[i] = Integer.toString(values[i]);
    return fromArr(results);
  }

  public String fromFloatArr(float... values) {
    String[] results = new String[values.length];
    for (int i = 0; i < values.length; i++)
      results[i] = Float.toString(values[i]);
    return fromArr(results);
  }

  public String fromArr(String... values) {
    return StringUtils.join(values, ",");
  }

  public void set(String key, String value) { section.put(key, value); }

  public void copy(ValueMap map) {
    copy(map.section);
  }

  public void copy(Map<String, String> section) {
    section.forEach(this::set);
  }

  public void clear() {
    section.clear();
  }
}
