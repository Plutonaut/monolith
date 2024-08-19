package com.game.engine.render.mesh;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;


@Accessors(fluent = true)
@Getter
@Setter
public class FontMeshInfo extends MeshInfo {
  protected Font font;
  protected String text;
  public FontMeshInfo(String name, String text, Font font) {
    super(name);

    this.font = font;
    this.text = text;
  }
}
