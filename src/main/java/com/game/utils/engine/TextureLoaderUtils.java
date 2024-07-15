package com.game.utils.engine;

import com.game.graphics.texture.Texture;
import com.game.loaders.ITextureReader;
import com.game.utils.application.PathSanitizer;
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

public class TextureLoaderUtils {
  public static void readTexture(String path, ITextureReader reader) {
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

  public static Texture generate(String path, BufferedImage image, boolean save) {
    if (save) {
      try {
        File f = new File(PathSanitizer.sanitizeFilePath(path));
        ImageIO.write(image, "png", f);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    ByteBuffer buf = null;
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
        throw new RuntimeException("Failed to load texture from memory!" + System.lineSeparator()
                                     + STBImage.stbi_failure_reason());
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
