package com.game.engine.scene.entities;

import com.game.utils.enums.EGUIEvent;

public interface IEntityInteractionCallback {
  void onEvent(EGUIEvent state);
}
