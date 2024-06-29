package com.game.engine.scene.entities.animations.audio;

import com.game.graphics.IGraphicsCachable;
import com.game.utils.enums.EGraphicsCache;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.lwjgl.openal.AL11;

import java.nio.ShortBuffer;

@Accessors(fluent = true)
@Getter
public class AudioBufferObject implements IGraphicsCachable {
  private final String path;
  private final int alId;

  public AudioBufferObject(String path) {
    alId = AL11.alGenBuffers();

    this.path = path;
  }

  public void upload(ShortBuffer pcm, int channels, int sampleRate) {
    int channel = channels == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16;
    AL11.alBufferData(alId, channel, pcm, sampleRate);
  }

  @Override
  public int glId() {
    return alId;
  }

  @Override
  public void bind() {

  }

  public void dispose() {
    AL11.alDeleteBuffers(alId);
  }

  @Override
  public EGraphicsCache type() {
    return EGraphicsCache.AUDIO;
  }

  @Override
  public String key() {
    return path;
  }
}
