package com.game.engine.render.renderers.concrete;

import com.game.engine.render.IRenderable;
import com.game.engine.render.renderers.AbstractRenderer;
import com.game.engine.scene.Scene;
import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.window.Window;
import com.game.utils.application.LambdaCounter;
import com.game.utils.enums.ERenderer;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;

@Slf4j
public class RaytraceRenderer extends AbstractRenderer {
  LambdaCounter counter = new LambdaCounter();

  @Override
  public ERenderer type() {
    return ERenderer.RAYTRACE;
  }

  @Override
  protected void render(IRenderable item, Scene scene) {
    Entity entity = (Entity) item;
    Camera camera = scene.camera();
    Window window = scene.window();

    Camera.ViewVectors view = camera.viewVectors();

    setViewerUniform("position", view.position());
    setViewerUniform("forwards", view.forwards());
    setViewerUniform("right", view.right());
    setViewerUniform("up", view.up());

    entity.instructions().forEach(in -> {
      // If statement does not improve performance.
      if (in.read()) {
        program.uniforms().set("sphereCount", in.uniforms().getInt("sphereCount"));
        in.write(counter.inc());
      }
      // Best worker combination remains x = 8, y = 4 (~150FPS) vs x = 8, y = 8 (~115FPS)
      int xWorkers = window.width() / 8;
      int yWorkers = window.height() / 4;
      // Frame rate nosedives here from ~600 down to ~125-150
      in.compute(xWorkers, yWorkers);
    });

    counter.set(0);
  }

  void setViewerUniform(String uniform, Vector3f value) {
    program.uniforms().set("viewer." + uniform, value);
  }
}
