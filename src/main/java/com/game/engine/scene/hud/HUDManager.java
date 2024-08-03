package com.game.engine.scene.hud;

import com.game.engine.scene.entities.TextEntity;

import java.util.HashMap;

public class HUDManager {
  protected final HashMap<String, TextEntity> hud;

  public HUDManager() {
    hud = new HashMap<>();
  }
}
