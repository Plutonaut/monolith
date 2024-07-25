package com.game.engine.scene.audio;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

@Accessors(fluent = true)
@Data
public class AudioSource {
  private final Vector3f position;
  private final int alId;
  private String name;

  public AudioSource(String name) {
    alId = AL11.alGenSources();
    position = new Vector3f();

    this.name = name;
  }

  public void buffer(AudioBufferObject abo) {
    AL11.alSourcei(alId, AL11.AL_BUFFER, abo.alId());
  }

  public void play() {
    AL11.alSourcePlay(alId);
  }

  public void pause() {
    AL11.alSourcePause(alId);
  }

  public void stop() {
    AL11.alSourceStop(alId);
  }

  public boolean isPlaying() {
    return AL11.alGetSourcei(alId, AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING;
  }

  public void loop(boolean toggle) {
    toggle(toggle, AL11.AL_LOOPING);
  }

  public void relative(boolean toggle) {
    toggle(toggle, AL11.AL_SOURCE_RELATIVE);
  }

  public void moveTo(Vector3f position) {
    this.position.set(position);
    AL11.alSource3f(alId, AL11.AL_POSITION, position.x, position.y, position.z);
  }

  public void gain(float amount) {
    AL11.alSourcef(alId, AL11.AL_GAIN, amount);
  }

  public float gain() {return AL11.alGetSourcef(alId, AL11.AL_GAIN);}

  public void toggle(boolean toggle, int type) {
    int alToggle = toggle ? AL11.AL_TRUE : AL11.AL_FALSE;

    AL11.alSourcei(alId, type, alToggle);
  }

  public void dispose() {
    stop();
    AL11.alDeleteSources(alId);
  }
}
