package com.game.engine.scene.entities.animations;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public abstract class Animation {
  protected String name;
  protected int current;

  public abstract int count();

  public void set(int index) {current = Math.max(0, index % count());}
  public void move(int steps) {set(current + steps); }
}
