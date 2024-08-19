package com.game.engine.scene.entities.controllers;

import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.entities.transforms.ModelTransform;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public abstract class AbstractEntityController {

  public abstract String type();

  public abstract void onUpdate(ModelTransform transform);

  public IMeshUpdater updater;

  public interface IMeshUpdater {
    Mesh requestOrCreate(int glId, IMeshGenerator requester);
  }

  public interface IMeshGenerator {
    Mesh generate();
  }
}
