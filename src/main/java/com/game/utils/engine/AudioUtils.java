package com.game.utils.engine;

import com.game.engine.scene.audio.AudioBufferObject;
import com.game.utils.application.PathSanitizer;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class AudioUtils {
  public static AudioBufferObject load(String path) {
    try (STBVorbisInfo info = STBVorbisInfo.malloc(); MemoryStack stack = MemoryStack.stackPush()) {
      String filePath = PathSanitizer.sanitizeFilePath(path, "sounds");

      IntBuffer error = stack.mallocInt(1);
      long decoder = STBVorbis.stb_vorbis_open_filename(filePath, error, null);

      if (decoder == MemoryUtil.NULL)
        throw new RuntimeException("Failed to open Vorbis file " + filePath + " Error: " + error.get(
          0));

      STBVorbis.stb_vorbis_get_info(decoder, info);

      int channels = info.channels();
      int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);

      ShortBuffer result = MemoryUtil.memAllocShort(lengthSamples);
      int sampleRate = STBVorbis.stb_vorbis_get_samples_short_interleaved(
        decoder,
        channels,
        result
      ) * channels;

      result.limit(sampleRate);
      STBVorbis.stb_vorbis_close(decoder);

      AudioBufferObject abo = new AudioBufferObject(path);
      abo.upload(result, channels, info.sample_rate());

      MemoryUtil.memFree(result);

      return abo;
    }
  }
}
