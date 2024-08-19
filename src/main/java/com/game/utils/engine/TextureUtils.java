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
  public static final String HDRI_DIRECTORY = TEXTURE_DIRECTORY + "hdri/";
  public static final String HDRI_A = HDRI_DIRECTORY + "dikhololo_night_4k.hdr";
  public static final String HDRI_B = HDRI_DIRECTORY + "kloppenheim_02_4k.hdr";
  public static final String HDRI_C = HDRI_DIRECTORY + "pump_station_4k.hdr";
  public static final String HDRI_D = HDRI_DIRECTORY + "satara_night_8k.hdr";
  public static final String SKYBOX_A = TEXTURE_DIRECTORY + "shared/skybox_attempt_1.png";
  public static final String SKYBOX_B = TEXTURE_DIRECTORY + "shared/skybox_attempt_2.png";
}
