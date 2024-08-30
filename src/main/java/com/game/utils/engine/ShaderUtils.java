package com.game.utils.engine;

import com.game.caches.GlobalCache;
import com.game.graphics.shaders.IShaderBuilder;
import com.game.graphics.shaders.Shader;
import com.game.utils.application.LoaderUtils;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ShaderUtils {
  static final String PARENT_DIR = "render";
  static final String SHADER_VERS = "#version 460 core";
  public static ERenderer getShaderEnumFromKey(String key) {
    return EnumSet
      .allOf(ERenderer.class)
      .stream()
      .filter(s -> s.key().equals(key))
      .findFirst()
      .orElse(null);
  }

  public static Shader[] getShaders(ERenderer type) {
    return createShaders(type, Shader::new);
  }

  public static Shader[] getShadersFromCache(String key) {
    ERenderer renderer = getShaderEnumFromKey(key);

    if (renderer == null)
      throw new IllegalArgumentException("Could not find shader enum from program key " + key);
    return getShadersFromCache(renderer);
  }

  public static Shader[] getShadersFromCache(ERenderer type) {
    return createShaders(type, ShaderUtils::getShader);
  }

  public static Shader[] createShaders(ERenderer type, IShaderBuilder builder) {
    return type.paths().stream().map(builder::build).toArray(Shader[]::new);
  }

  static Shader getShader(String path) { return GlobalCache.instance().shader(path); }

  public static String parentDirFromShaderType(int type) {
    return type == GL46.GL_COMPUTE_SHADER ? "compute" : PARENT_DIR;
  }

  public static int shaderTypeFromFileType(String fileType) {
    String type = fileType;
    if (!type.startsWith(".") && type.contains(".")) type = LoaderUtils.getFileType(type);
    if (type == null) return -1;
    type = type.replace(".", "");
    return switch (type) {
      case "geom" -> GL46.GL_GEOMETRY_SHADER;
      case "vert", "vt" -> GL46.GL_VERTEX_SHADER;
      case "frag", "fg" -> GL46.GL_FRAGMENT_SHADER;
      default -> GL46.GL_COMPUTE_SHADER;
    };
  }

  public static List<VaryingRecord> shaderTransformVarryings(String shader) {
    List<VaryingRecord> varyings = new ArrayList<>();
    if (shader.equals(ERenderer.PARTICLE.key())) varyings.add(new VaryingRecord(
      true,
      "type1",
      "position1",
      "velocity1",
      "age1"
    ));

    return varyings;
  }

  public record VaryingRecord(boolean interleaved, String... varyings) {
  }
}
