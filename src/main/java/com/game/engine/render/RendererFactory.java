package com.game.engine.render;

import com.game.caches.GlobalCache;
import com.game.engine.render.renderers.*;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.utils.engine.ShaderUtils;
import com.game.utils.enums.ERenderer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RendererFactory {
  public AbstractRenderer create(ERenderer type) {
    Shader[] shaders = ShaderUtils.getShadersFromCache(type);
    Program program = GlobalCache.instance().program(type.key());
    program.link(shaders);
    return switch (type) {
      case MESH -> new MeshRenderer();
      case SCENE -> new SceneRenderer();
      case SKYBOX -> new SkyBoxRenderer();
      case SPRITE -> new SpriteRenderer();
      case BASIC -> new BasicRenderer();
    };
  }
}
