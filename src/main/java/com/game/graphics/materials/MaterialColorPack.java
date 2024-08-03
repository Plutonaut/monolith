package com.game.graphics.materials;

import com.game.utils.engine.ColorUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIColor4D;

import java.awt.*;
import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class MaterialColorPack {
  protected final HashMap<String, Vector4f> colors;

  public MaterialColorPack() {
    colors = new HashMap<>();
  }

  public Vector4f get(String type) {
    return colors.get(type);
  }

  public void add(String type, Color color) {
    add(type, ColorUtils.convert(color));
  }

  public void add(String type, AIColor4D color) {
    add(type, ColorUtils.convert(color));
  }

  public void add(String type, Vector4f color) {
    colors.put(type, color);
  }

  public boolean hasColor(String type) {
    return colors.containsKey(type) && get(type) != null;
  }
}
