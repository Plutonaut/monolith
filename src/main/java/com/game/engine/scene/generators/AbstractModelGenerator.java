package com.game.engine.scene.generators;

import com.game.engine.render.models.Model;
import com.game.engine.scene.generators.data.AbstractGenerationData;

public abstract class AbstractModelGenerator<T extends AbstractGenerationData> {
//  public Model generate(T data) {
//    return GlobalCache.instance().model(data.id(), id -> generateModel(data));
//  }

  public abstract Model generateModel(T data);
}
