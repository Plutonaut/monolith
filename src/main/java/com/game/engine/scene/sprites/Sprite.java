package com.game.engine.scene.sprites;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@NoArgsConstructor
@Data
public class Sprite {
  protected String name;
  protected String path;
  protected int frame;

  public Sprite(String name, String path) {
    this.name = name;
    this.path = path;

    frame = -1;
  }

  public void setName(String name) { this.name = name; }

  public void setPath(String path) { this.path = path; }

  public void setFrame(int frame) { this.frame = frame; }
}
