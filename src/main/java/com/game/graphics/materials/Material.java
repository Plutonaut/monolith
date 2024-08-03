package com.game.graphics.materials;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector4f;

@Accessors(fluent = true)
@Data
public class Material {
  protected final MaterialTexturePack textures;
  protected final MaterialColorPack colors;
  protected String name;
  protected float reflectance;

  public Material(String name) {
    this.name = name;
    textures = new MaterialTexturePack();
    colors = new MaterialColorPack();
    reflectance = 0f;
  }

  public String texture(int type) {
    return textures.get(type);
  }

  public void texture(int type, String texture) {
    textures.add(type, texture);
  }

  public Vector4f color(String type) { return colors.get(type); }

  public void color(String type, Vector4f color) {
    colors.add(type, color);
  }
}
