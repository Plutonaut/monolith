package com.game.engine.render;

import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.render.renderers.concrete.*;
import com.game.utils.enums.ERenderer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RendererFactory {
  public AbstractRenderer create(ERenderer type) {
    return switch (type) {
      case MESH -> new MeshRenderer();
      case SCENE -> new SceneRenderer();
      case SKYBOX -> new SkyBoxRenderer();
      case SPRITE -> new SpriteRenderer();
      case FONT -> new FontRenderer();
      case BASIC -> new BasicRenderer();
    };
  }
}
