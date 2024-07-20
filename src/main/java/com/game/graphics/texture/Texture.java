package com.game.graphics.texture;

import com.game.caches.graphics.IGraphicsCachable;
import com.game.utils.enums.EGraphicsCache;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.nio.ByteBuffer;

@Accessors(fluent = true)
@Data
public class Texture implements IGraphicsCachable {
  protected final int glId;
  protected final String path;
  protected int width;
  protected int height;

  public Texture(String path) {
    glId = GL46.glGenTextures();

    this.path = path;
  }

  @Override
  public String key() {
    return path;
  }

  public void active(int glIndex) {
    GL46.glActiveTexture(glIndex);
  }

  @Override
  public void bind() {
    GL46.glBindTexture(GL46.GL_TEXTURE_2D, glId);
  }

  @Override
  public void dispose() {
    GL46.glDeleteTextures(glId);
  }

  public void store() {
    GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
  }

  public void parameter(int name, int value) {
    GL46.glTexParameteri(GL46.GL_TEXTURE_2D, name, value);
  }

  public void clamp() {
    parameter(GL46.GL_TEXTURE_WRAP_S, GL46.GL_CLAMP_TO_BORDER);
    parameter(GL46.GL_TEXTURE_WRAP_T, GL46.GL_CLAMP_TO_BORDER);
  }

  public void filter() {
    parameter(GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
    parameter(GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
  }

  public void upload(int internalFormat, int format, int glType, ByteBuffer image) {
    GL46.glTexImage2D(
      GL46.GL_TEXTURE_2D,
      0,
      internalFormat,
      width,
      height,
      0,
      format,
      glType,
      image
    );
    GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
  }

  @Override
  public EGraphicsCache type() {
    return EGraphicsCache.TEXTURE;
  }
}
