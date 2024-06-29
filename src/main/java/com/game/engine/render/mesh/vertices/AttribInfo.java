package com.game.engine.render.mesh.vertices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class AttribInfo {
  protected String key;
  protected int size;
  protected int instances;
}
