package com.game.graphics.materials;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class MaterialTexturePack {
  protected final HashMap<Integer, String> pack;

  public MaterialTexturePack() {
    pack = new HashMap<>();
  }

  public MaterialTexturePack add(int type, String texture) {
    pack.put(type, texture);
    return this;
  }

  public String get(int type) {
    return pack.getOrDefault(type, null);
  }

  public boolean hasTexture(int type) {
    String texture = get(type);
    return texture != null && !texture.isEmpty();
  }

  public boolean hasTextures() {
    return pack.keySet().stream().anyMatch(this::hasTexture);
  }
}
