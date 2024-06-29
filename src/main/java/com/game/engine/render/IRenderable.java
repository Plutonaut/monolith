package com.game.engine.render;

import com.game.utils.enums.ERenderer;

import java.util.Collection;

public interface IRenderable {
//  boolean matches(ERenderer renderer);
  Collection<ERenderer> shaders();
}
