package com.game.caches.graphics;

import com.game.graphics.texture.Texture;
import com.game.utils.application.LoaderUtils;
import com.game.utils.engine.TextureLoaderUtils;
import org.lwjgl.opengl.GL46;

public class TextureCache extends AbstractGraphicsCache {
  @Override
  protected Texture generate(String path) {
    Texture texture = new Texture(path);
    texture.bind();
    texture.store();
    texture.clamp();
    texture.mipmap();

    String fileType = LoaderUtils.getFileType(path);
    int format = fileType != null ? TextureLoaderUtils.formatByFileType(fileType) : GL46.GL_RGBA;

    TextureLoaderUtils.readTexture(path, ((buffer, width, height) -> {
      texture.width(width);
      texture.height(height);
      texture.upload(GL46.GL_RGBA, format, GL46.GL_UNSIGNED_BYTE, buffer);
    }));

    return texture;
  }
}
