package com.game.engine.scene.generators;

import com.game.engine.render.models.Model;

public interface IGeneratorConsumer {
  Model consume(ModelGenerator generator);
}
