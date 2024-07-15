package com.game.engine.render.mesh;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@Setter
public class FontMeshInfo extends MeshInfo {
  protected String text;
  public FontMeshInfo(String name, String text) {
    super(name);

    this.text = text;
  }
}
