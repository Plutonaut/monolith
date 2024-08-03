package com.game.utils.engine;

import com.game.graphics.texture.TextureMapData;
import com.game.utils.engine.entity.StaticEntityData;

public class TextureUtils {
  static final String TEXTURE_DIRECTORY = StaticEntityData.RESOURCE_DIRECTORY + "textures/";
  static final String MOSS_TEXTURE_DIRECTORY = TEXTURE_DIRECTORY + "moss_surface/";
  static final String MOSS_TEXTURE_FILE_PATH_BASE = MOSS_TEXTURE_DIRECTORY + "moss_surface_%s.png";
  public static final String MOSS_DIFF_TEXTURE_PATH = MOSS_TEXTURE_FILE_PATH_BASE.formatted("albedo");
  public static final String MOSS_NORM_TEXTURE_PATH = MOSS_TEXTURE_FILE_PATH_BASE.formatted("normal");
  public static final String MOSS_HGHT_TEXTURE_PATH = MOSS_TEXTURE_FILE_PATH_BASE.formatted("height");
  public static final TextureMapData MOSS_TEXTURE_MAP_DATA = new TextureMapData()
    .diffuse(MOSS_DIFF_TEXTURE_PATH)
    .normal(MOSS_NORM_TEXTURE_PATH)
    .height(MOSS_HGHT_TEXTURE_PATH);
}
