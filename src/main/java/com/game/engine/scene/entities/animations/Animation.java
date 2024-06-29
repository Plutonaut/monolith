package com.game.engine.scene.entities.animations;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Data
public class Animation {
  private final List<Frame> frames;

  private String name;
  private double duration;
  private int current;

  public Animation(String name, double duration, List<Frame> frames) {
    this.name = name;
    this.duration = duration;
    this.frames = frames;

    current = 0;
  }

  public int count() {return frames.size();}
  public Frame frame() {return frames.get(current);}
  public Frame get(int index) {return frames.get(index);}
  public void set(int index) {current = Math.max(0, index % count());}
  public void move(int steps) {set(current + steps); }
}
