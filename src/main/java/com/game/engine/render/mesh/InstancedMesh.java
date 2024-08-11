package com.game.engine.render.mesh;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

@Accessors(fluent = true)
@Getter
@Setter
public class InstancedMesh extends Mesh {
  protected int instances;

  public InstancedMesh(String name, int instances) {
    super(name);

    this.instances = instances;
  }

  @Override
  protected void drawSimple(int mode) {
    GL46.glDrawArraysInstanced(mode, 0, vertexCount, instances);
  }

  @Override
  protected void drawComplex(int mode) {
    GL46.glDrawElementsInstanced(mode, vertexCount, GL46.GL_UNSIGNED_INT, 0, instances);
  }
}
