package com.game.engine.scene.entities.animations;

import com.game.engine.scene.sprites.Sprite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Animation2D extends Animation {
  protected final ArrayList<Sprite> spriteFrames;

  public Animation2D(String name, List<Sprite> spriteFrames) {
    this.name = name;
    this.spriteFrames = new ArrayList<>();
    spriteFrames.forEach(this::add);
    sort();

    current = 0;
  }

  @Override
  public int count() {
    return 0;
  }

  public Sprite frame() { return get(current); }

  public Sprite get(int index) {
    return spriteFrames.get(index);
  }

  void add(Sprite frame) {
    if (frame.frame() >= 0) spriteFrames.add(frame.frame(), frame);
  }

  public void sort() {
    spriteFrames.sort(Comparator.comparingInt(Sprite::frame));
  }
}
