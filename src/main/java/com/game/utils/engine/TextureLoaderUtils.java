package com.game.utils.engine;

import com.game.caches.GlobalCache;
import com.game.graphics.texture.Texture;
import com.game.loaders.ITextureBufferReader;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.PathSanitizer;
import com.game.utils.application.ValueStore2D;
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
import java.nio.file.Files;
import java.nio.file.Path;

public class TextureLoaderUtils {
  public static Texture load(String path) {
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

  public static void readTexture(String path, ITextureBufferReader reader) {
    ByteBuffer buffer;

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer comp = stack.mallocInt(1);

      STBImage.stbi_set_flip_vertically_on_load(true);
      buffer = STBImage.stbi_load(path, w, h, comp, 4);

      if (buffer == null)
        throw new RuntimeException("Texture path: " + path + System.lineSeparator() + STBImage.stbi_failure_reason());

      reader.read(buffer, w.get(), h.get());

      STBImage.stbi_image_free(buffer);
    }
  }

  /**
   * Convert two-dimensional array of floats into color data and create a texture from the resulting
   * buffered image.
   *
   * @param path Relative file path of the texture to be generated.
   * @param grid 2D array of floats to be converted into pixel color data.
   * @param save flag denoting whether the resulting texture should be saved to the file system.
   *
   * @return resulting texture.
   */
  public static Texture generate(String path, ValueStore2D grid, boolean save) {
    int rows = grid.height();
    int columns = grid.width();

    BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++) {
        float heightValue = grid.get(y, x);
        int colorValue = ColorUtils.interpolate(heightValue);
        image.setRGB(x, y, colorValue);
      }
    }

    return generate(path, image, save);
  }

  public static Texture generate(String path, BufferedImage image, boolean save) {
    if (Files.exists(Path.of(path))) return GlobalCache.instance().texture(path);

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

    return generate(path, buf);
  }

  public static Texture generate(String path, ByteBuffer buffer) {
    if (Files.exists(Path.of(path))) return GlobalCache.instance().texture(path);

    Texture texture = new Texture(path);
    texture.bind();
    texture.store();
    texture.clamp();
    texture.mipmap();
    ByteBuffer decodedImage;
    int width;
    int height;
    try (MemoryStack stack = MemoryStack.stackPush()) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer avChannels = stack.mallocInt(1);
      // Sets the texture origin to the bottom left instead of the top left
      STBImage.stbi_set_flip_vertically_on_load(true);
      // Decode texture image into a byte buffer
      decodedImage = STBImage.stbi_load_from_memory(buffer, w, h, avChannels, 4);
      if (decodedImage == null)
        throw new RuntimeException("Failed to load texture from memory!" + System.lineSeparator() + STBImage.stbi_failure_reason());
      width = w.get();
      height = h.get();
      texture.width(width);
      texture.height(height);
      STBImage.stbi_image_free(decodedImage);
    }
    return texture;
  }

  public static int formatByFileType(String fileType) {
    if (fileType.equals(".tga")) return GL46.GL_BGRA;

    return GL46.GL_RGBA;
  }
}
