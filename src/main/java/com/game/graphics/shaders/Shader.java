package com.game.graphics.shaders;

import com.game.graphics.IGraphics;
import com.game.utils.application.LoaderUtils;
import com.game.utils.engine.ShaderUtils;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

@Accessors(fluent = true)
@Data
public class Shader implements IGraphics {
  protected int glId;
  protected String key;
  protected String content;

  public Shader(String path) {
    key = path;

    int type = ShaderUtils.shaderTypeFromFileType(path);

    if (type < 0) throw new RuntimeException("Could not load shader from path: " + path);
    glId = GL46.glCreateShader(type);
    if (glId == 0) throw new RuntimeException("Error creating shader from path: " + path);
    content = LoaderUtils.load(path, "shaders");
    source(content);
    bind();
  }

  public void source(String content) {
    GL46.glShaderSource(glId, content);
  }

  @Override
  public void bind() {
    GL46.glCompileShader(glId);

    int status = GL46.glGetShaderi(glId, GL46.GL_COMPILE_STATUS);
    if (status != GL46.GL_TRUE) throw new RuntimeException("GL Status: " + status + " Shader Info Log: " + GL46.glGetShaderInfoLog(glId));
  }

  @Override
  public void dispose() {
    GL46.glDeleteShader(glId);
  }

  @Override
  public ECache type() {
    return ECache.SHADER;
  }
}
