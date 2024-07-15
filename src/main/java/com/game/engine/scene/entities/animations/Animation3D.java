package com.game.engine.scene.entities.animations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class Animation3D extends Animation {
  private final List<BoneMatrixFrame> boneMatrixFrames;
  protected double duration;

  public Animation3D(String name, double duration, List<BoneMatrixFrame> boneMatrixFrames) {
    this.name = name;
    this.duration = duration;
    this.boneMatrixFrames = boneMatrixFrames;

    current = 0;
  }

  @Override
  public int count() { return boneMatrixFrames.size(); }

  public BoneMatrixFrame frame() { return get(current); }

  public BoneMatrixFrame get(int index) { return boneMatrixFrames.get(index); }
}
