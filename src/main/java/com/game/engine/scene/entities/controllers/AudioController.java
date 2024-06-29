package com.game.engine.scene.entities.controllers;

import com.game.engine.scene.entities.animations.audio.AudioSourceObject;
import com.game.utils.enums.EController;

import java.util.HashMap;

public class AudioController extends AbstractController {
  protected final HashMap<String, AudioSourceObject> sources;

  public AudioController() {
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
