package com.game.graphics.texture;

import com.game.graphics.IGraphics;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.nio.ByteBuffer;

@Accessors(fluent = true)
@Data
public class Texture implements IGraphics {
  protected final int glId;
  protected final String path;
  protected final int target;
  protected int width;
  protected int height;

  public Texture(String path) {
    this(path, 0, 0);
  }

  public Texture(String path, int width, int height) {
    this(path, width, height, GL46.GL_TEXTURE_2D);
  }

  public Texture(String path, int width, int height, int target) {
    glId = GL46.glGenTextures();

    this.path = path;
    this.width = width;
    this.height = height;
    this.target = target;
  }

  @Override
  public String key() {
    return path;
  }

  public void active(int glIndex) {
    GL46.glActiveTexture(GL46.GL_TEXTURE0 + glIndex);
  }

  @Override
  public void bind() {
    GL46.glBindTexture(target, glId);
  }

  @Override
  public void dispose() {
    GL46.glDeleteTextures(glId);
  }

  // Tells OpenGL to unpack the RGBA data with 1 byte for each component.
  public void store() {
    GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
  }

  public void parameter(int name, int value) {
    GL46.glTexParameteri(GL46.GL_TEXTURE_2D, name, value);
  }

  public void clampBorder() {
    wrap(GL46.GL_CLAMP_TO_BORDER);
  }

  public void clampEdge() {
    wrap(GL46.GL_CLAMP_TO_EDGE);
  }

  public void repeat() {
    wrap(GL46.GL_REPEAT);
  }

  public void wrap(int value) {
    parameter(GL46.GL_TEXTURE_WRAP_S, value);
    parameter(GL46.GL_TEXTURE_WRAP_T, value);
  }

  public void nearest() {
    filter(GL46.GL_NEAREST);
  }

  public void linear() {
    filter(GL46.GL_LINEAR);
  }

  public void filter(int value) {
    parameter(GL46.GL_TEXTURE_MIN_FILTER, value);
    parameter(GL46.GL_TEXTURE_MAG_FILTER, value);
  }

  public void upload1D(int internalFormat, int format, int glType, float[] data) {
    GL46.glTexImage1D(target, 0, internalFormat, width, 0, format, glType, data);
    GL46.glGenerateMipmap(target);
  }

  public void upload(int internalFormat, int format, int glType, ByteBuffer image) {
    GL46.glTexImage2D(
      target,
      0,
      internalFormat,
      width,
      height,
      0,
      format,
      glType,
      image
    );
    GL46.glGenerateMipmap(target);
  }

  @Override
  public ECache type() {
    return ECache.TEXTURE;
  }
}
