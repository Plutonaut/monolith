package com.game.engine.scene.entities.animations;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;

@Accessors(fluent = true)
@Data
public class Frame {
  private Matrix4f[] boneMatrices;

  public Frame(Matrix4f... boneMatrices) {
    this.boneMatrices = boneMatrices;
  }
}
