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
public class Material {
  protected final MaterialTexturePack textures;
  protected final HashMap<String, Vector4f> colors;
  protected float reflectance;

  public Material() {
    textures = new MaterialTexturePack();
    colors = new HashMap<>();

    reflectance = 0f;
  }

  public String texture(int type) {
    return textures.get(type);
  }

  public void texture(int type, String texture) {
    textures.add(type, texture);
  }

  public Vector4f color(String type) {
    return colors.get(type);
  }

  public void color(String type, Color color) {
    color(type, ColorUtils.convert(color));
  }

  public void color(String type, AIColor4D color) {
    color(type, ColorUtils.convert(color));
  }

  public void color(String type, Vector4f color) {
    colors.put(type, color);
  }
}
