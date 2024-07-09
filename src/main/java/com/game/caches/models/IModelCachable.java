package com.game.caches.models;

import com.game.engine.render.models.IModel;
import com.game.utils.enums.EModelCache;

public interface IModelCachable extends IModel {
  EModelCache type();
}
