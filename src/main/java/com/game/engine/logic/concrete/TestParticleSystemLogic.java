package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.enums.EGLParam;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import org.joml.Vector3f;

public class TestParticleSystemLogic extends AbstractLogic {
  int numParticles = 0;
  Entity particleEntity;

  public TestParticleSystemLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "Particle System (%d)".formatted(numParticles);
  }

  @Override
  public void preRender(float delta) {
    scene.particles().onPreRender(delta);
  }

  @Override
  public void render(int fps) {
    super.render(fps);
    scene.particles().onPostRender();
  }

  @Override
  public void onStart() {
    hud.onStart();
    particleEntity = scene.createParticles("simple", new Vector3f());
    Entity renderParticleEntity = scene.addEntity(
      "render",
      particleEntity.meshes(),
      ERenderer.BILLBOARD,
      EProjection.PERSPECTIVE,
      EGLParam.BLEND,
      EGLParam.DEPTH
    ).scale(0.1f);
    scene.bind(renderParticleEntity, particleEntity);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
    hud.onInput();
  }
}
