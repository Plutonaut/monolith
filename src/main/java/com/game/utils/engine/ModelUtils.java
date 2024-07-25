package com.game.utils.engine;

public class ModelUtils {
  static final String MESH_INFO_NAME_SUFFIX = "_mesh_info";
  static final String MODEL_NAME_SUFFIX = "_model";
  static final String FONT_INFO_NAME_SUFFIX = "_font_info";

  public static String resolveModelName(String name) {
    return nameResolver(name, MODEL_NAME_SUFFIX);
  }

  public static String resolveMeshInfoName(String name) {
    return nameResolver(name, MESH_INFO_NAME_SUFFIX);
  }

  public static String resolveFontInfoName(String name) {
    return nameResolver(name, FONT_INFO_NAME_SUFFIX);
  }

  static String nameResolver(String name, String suffix) {
    return name.endsWith(suffix) ? name : name + suffix;
  }
}
