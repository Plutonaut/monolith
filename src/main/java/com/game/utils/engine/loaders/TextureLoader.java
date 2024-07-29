package com.game.utils.engine.loaders;

import com.game.graphics.texture.Texture;
import com.game.loaders.ITextureBufferCreator;
import com.game.loaders.ITextureBufferReader;
import com.game.loaders.ITextureBufferStrategy;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.PathSanitizer;
import com.game.utils.application.ValueGrid;
import com.game.utils.engine.ColorUtils;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Slf4j
public class TextureLoader {
  public static Texture load(String path) {
    return generate(path, (w, h, avChannels) -> STBImage.stbi_load(path, w, h, avChannels, 4));
  }

  public static Texture load(String path, ValueGrid grid) {
    int rows = grid.height();
    int columns = grid.width();
    BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        float heightValue = grid.get(x, y);
        int colorValue = ColorUtils.interpolate(heightValue);
        image.setRGB(x, y, colorValue);
      }
    }

    return load(path, image, true);
  }

  public static Texture load(String path, BufferedImage image, boolean save) {
    if (LoaderUtils.isResourcePath(path)) return load(path);

    if (save) {
      try {
        File f = new File(PathSanitizer.sanitizeFilePath(path));
        ImageIO.write(image, "png", f);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    ByteBuffer buf;
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      ImageIO.write(image, "png", out);
      out.flush();
      final byte[] bytes = out.toByteArray();
      buf = ByteBuffer.allocateDirect(bytes.length);
      buf.put(bytes, 0, bytes.length);
      buf.flip();
    } catch (final IOException ex) {
      throw new RuntimeException(ex);
    }

    return load(path, buf);
  }

  public static Texture load(String path, ByteBuffer buffer) {
    return generate(
      path,
      (w, h, avChannels) -> STBImage.stbi_load_from_memory(
        buffer,
        w,
        h,
        avChannels,
        4
      )
    );
  }

  public static Texture load(String path, ITextureBufferCreator creator) {
    return generate(
      path,
      (w, h, avChannels) -> STBImage.stbi_load(path, w, h, avChannels, 4),
      creator
    );
  }

  public static void read(String path, ITextureBufferReader reader) {
    generate(path, (w, h, avChannels) -> STBImage.stbi_load(path, w, h, avChannels, 4), (p, w, h, b) -> {
      reader.read(b, w, h);
      return null;
    });
  }

  static Texture generate(String path, ITextureBufferStrategy strategy) {
    return generate(path, strategy, TextureLoader::create);
  }

  static Texture generate(
    String path,
    ITextureBufferStrategy strategy,
    ITextureBufferCreator creator
  ) {
    log.info("Generating texture at path {}", path);
    ByteBuffer buffer;
    Texture texture;
    try (
      MemoryStack stack = MemoryStack.stackPush()
    ) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer avChannels = stack.mallocInt(1);
      // Sets the texture origin to the bottom left instead of the top left
      STBImage.stbi_set_flip_vertically_on_load(true);
      buffer = strategy.load(w, h, avChannels);
      if (buffer == null)
        throw new RuntimeException("Texture path: " + path + System.lineSeparator() + STBImage.stbi_failure_reason());
      int width = w.get();
      int height = h.get();
      texture = creator.create(path, width, height, buffer);
      STBImage.stbi_image_free(buffer);
    }
    return texture;
  }

  static Texture create(String path, int width, int height, ByteBuffer buffer) {
    Texture texture = new Texture(path, width, height);
    texture.bind();
    texture.store();
    texture.nearest();
    texture.repeat();
    texture.upload(GL46.GL_RGBA8, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);
    return texture;
  }
}
