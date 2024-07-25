package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.audio.AudioSource;
import com.game.utils.enums.EController;

import java.util.HashMap;

public class EntityAudioController extends AbstractEntityController {
  protected final HashMap<String, AudioSource> sources;

  public EntityAudioController() {
    sources = new HashMap<>();
  }

  @Override
  public String type() {
    return EController.AUDIO.getValue();
  }

  @Override
  public void onUpdate() {

  }
}
