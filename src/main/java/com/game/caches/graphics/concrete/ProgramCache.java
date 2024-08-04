package com.game.caches.graphics.concrete;

import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.utils.engine.ShaderUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProgramCache extends AbstractGraphicsCache {
  @Override
  protected Program generate(String key) {
    log.info("Generating program {}", key);
    Shader[] shaders = ShaderUtils.getShadersFromCache(key);
    Program program = new Program(key);
    program.link(shaders);
    return program;
  }
}
