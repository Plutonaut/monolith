package com.game.graphics.texture;

import com.game.utils.enums.EMaterialTexture;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class TextureMapData {
  private final HashMap<Integer, String> map;

  public TextureMapData() {
    map = new HashMap<>();
  }

  public String diffuse() { return texture(EMaterialTexture.DIF.value()); }

  public TextureMapData diffuse(String path) {
    return texture(EMaterialTexture.DIF.value(), path);
  }

  public String normal() { return texture(EMaterialTexture.NRM.value()); }

  public TextureMapData normal(String path) {
    return texture(EMaterialTexture.NRM.value(), path);
  }

  public String height() { return texture(EMaterialTexture.HGT.value()); }

  public TextureMapData height(String path) {
    return texture(EMaterialTexture.HGT.value(), path);
  }

  public String texture(int type) {
    return map.get(type);
  }

  public TextureMapData texture(int type, String path) {
    map.put(type, path);
    return this;
  }
}
