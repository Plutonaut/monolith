package com.game.utils.engine;

import com.game.caches.GlobalCache;
import com.game.graphics.shaders.IShaderBuilder;
import com.game.graphics.shaders.Shader;
import com.game.utils.application.LoaderUtils;
import com.game.utils.enums.ERenderer;
import org.lwjgl.opengl.GL46;

public class ShaderUtils {
  public static Shader[] getShaders(ERenderer type) {
    return createShaders(type, Shader::new);
  }

  public static Shader[] getShadersFromCache(ERenderer type) {
    return createShaders(type, ShaderUtils::getShader);
  }

  public static Shader[] createShaders(ERenderer type, IShaderBuilder builder) {
    return type.paths().stream().map(builder::build).toArray(Shader[]::new);
  }

  static Shader getShader(String path) {return GlobalCache.instance().shader(path);}

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
}
