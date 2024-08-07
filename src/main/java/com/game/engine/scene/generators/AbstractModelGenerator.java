package com.game.engine.scene.generators;

import com.game.engine.render.models.Model;
import com.game.utils.application.values.ValueMap;

public abstract class AbstractModelGenerator {

  public abstract Model generateModel(ValueMap data);
}
