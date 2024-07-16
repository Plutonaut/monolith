package com.game.engine.scene.entities.animations;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;

@Accessors(fluent = true)
@Data
public class BoneMatrixFrame {
  private Matrix4f[] boneMatrices;

  public BoneMatrixFrame(Matrix4f... boneMatrices) {
    this.boneMatrices = boneMatrices;
  }
}
