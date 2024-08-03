package com.game.utils.engine.entity;

import com.game.graphics.texture.TextureMapData;

public class StaticEntityData {
  static final String RESOURCE_DIRECTORY = "src/main/resources/";
  static final String TEXTURE_DIRECTORY = RESOURCE_DIRECTORY + "textures/";
  static final String MOSS_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "moss_surface/";

  public static final TextureMapData MOSS_TEXTURE_MAP_DATA = new TextureMapData()
    .diffuse(MOSS_TEXTURE_DIRECTORY + "moss_surface_albedo.png")
    .normal(MOSS_TEXTURE_DIRECTORY + "moss_surface_normal.png")
    .height(MOSS_TEXTURE_DIRECTORY + "moss_surface_height.png");
}
