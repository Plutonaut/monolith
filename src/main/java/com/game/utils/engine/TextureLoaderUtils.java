package com.game.utils.engine;

import com.game.loaders.ITextureReader;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

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

  public static int formatByFileType(String fileType) {
    if (fileType.equals(".tga")) return GL46.GL_BGRA;

    return GL46.GL_RGBA;
  }
}
