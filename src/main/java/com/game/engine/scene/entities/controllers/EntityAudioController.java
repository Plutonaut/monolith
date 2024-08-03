package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EController;

import java.util.HashMap;

public class EntityAudioController extends AbstractEntityController {
  protected final HashMap<String, AudioSource> sources;

  public EntityAudioController() {
    sources = new HashMap<>();
  }

  @Override
  public String type() {
    return EController.AUDIO.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {
    sources.values().forEach(v -> v.moveTo(transform.position()));
  }

  public void add(AudioSource source) {
    sources.put(source.name(), source);
  }

  public void play(String key) {
    get(key).play();
  }

  public void pause(String key) {
    get(key).pause();
  }

  public void stop() {
    sources.values().forEach(AudioSource::stop);
  }

  public void stop(String key) {
    get(key).stop();
  }

  public void loop(String key, boolean toggle) {
    get(key).loop(toggle);
  }

  public AudioSource get(String key) {
    return sources.get(key);
  }
}
