package com.game.graphics.shaders;

import com.game.caches.graphics.IGraphicsCachable;
import com.game.caches.shaders.AttributeCache;
import com.game.caches.shaders.UniformCache;
import com.game.utils.engine.ShaderUtils;
import com.game.utils.enums.EGraphicsCache;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opengl.GL46;

import java.util.Arrays;

@Accessors(fluent = true)
@Data
@Slf4j
public class Program implements IGraphicsCachable {
  protected final AttributeCache attributes;
  protected final UniformCache uniforms;
  protected final int glId;
  protected final String key;

  public Program(String key) {
    glId = GL46.glCreateProgram();
    if (glId == 0) throw new RuntimeException("Failed to create shader program " + key);

    this.key = key;
    attributes = new AttributeCache(glId);
    uniforms = new UniformCache(glId);
  }

  public Program(ERenderer type) {
    this(type.key());

    link(ShaderUtils.getShaders(type));
  }

  public void link(Shader... shaders) {
    Arrays.stream(shaders).forEach(this::attach);
    GL46.glLinkProgram(glId);
    status(GL46.GL_LINK_STATUS, "Failed to link shader code", true, shaders);
    Arrays.stream(shaders).forEach(this::detach);
    status(GL46.GL_VALIDATE_STATUS, "Failed to validate shader code", false, shaders);
  }

  void attach(Shader shader) {
    GL46.glAttachShader(glId, shader.glId());
  }

  void detach(Shader shader) {
    GL46.glDetachShader(glId, shader.glId());
  }

  void status(int code, String message, boolean errorOnFail, Shader... shaders) {
    if (GL46.glGetProgrami(glId, code) == GL46.GL_FALSE) {
      StringBuilder msgBuilder = new StringBuilder("ERROR! Program: {%s} Message: %s".formatted(
        key,
        message
      )).append(
        GL46.glGetProgramInfoLog(glId, 255));
      if (errorOnFail) {
        for (Shader shader : shaders)
          msgBuilder.append(System.lineSeparator())
                    .append(shader.key)
                    .append(System.lineSeparator())
                    .append(shader.content);
        throw new RuntimeException(msgBuilder.toString());
      } else log.warn(msgBuilder.toString());
    }
  }

  @Override
  public void bind() {
    GL46.glUseProgram(glId);
  }

  public void dispose() {
    unbind();
    GL46.glDeleteProgram(glId);
  }

  // frog - move to global class?
  public void unbind() {
    GL46.glUseProgram(0);
  }

  @Override
  public EGraphicsCache type() {
    return EGraphicsCache.PROGRAM;
  }
}
