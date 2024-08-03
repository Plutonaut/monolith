package com.game.engine.logic;

import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.enums.EAudio;
import com.game.utils.enums.EModel;
import com.game.utils.math.ScalarUtils;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

@Slf4j
public class TestProceduralGenerationLogic extends AbstractLogic {
  Entity cubeEntity;
  Entity cubeEntityCopy;
  AudioSource musicSource;
  float tick = 0;

  public TestProceduralGenerationLogic(EngineSettings settings) {
    super(settings);
  }

  @Override
  protected String windowTitle() {
    return "Test Procedural Generation";
  }

  @Override
  public void onStart() {
    cubeEntity = scene
      .createModel(EModel.CUBE.name(), EModel.CUBE.path())
      .scale(0.25f)
      .move(0f, 0f, 1f);
    cubeEntityCopy = scene.copy(cubeEntity).scale(0.5f).move(1f, 0f, 1f);
    Entity skyboxEntity = scene.createSkyBox("skybox", EModel.BASIC_SKYBOX.path()).scale(100f);
    Entity terrainEntity = scene.generateProceduralTerrain("proc_moss", 128).addPhysics().scale(10);

    musicSource = scene.audio("Creepy music", EAudio.MUSIC_WOO.value());
    scene.bind(cubeEntity, cubeEntityCopy, skyboxEntity, terrainEntity);

    scene.window().keyboard().onKeyPress(GLFW.GLFW_KEY_M, (action) -> {
      if (action == GLFW.GLFW_PRESS) {
        log.info("M pressed!");
        if (musicSource.isPlaying()) musicSource.stop();
        else musicSource.play();
      }
    });

    scene.window().keyboard().onKeyPress(GLFW.GLFW_KEY_P, (action) -> {
      if (action == GLFW.GLFW_PRESS) scene.captureDiagnostics();
    });
  }

  @Override
  public void input() {
    captureCameraMovementInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
    Vector3f cubePosition = cubeEntity.transform().position();
    tick += 0.025f;
    cubePosition.y = ScalarUtils.lerp(0.5f, 1.0f, (float) Math.sin(tick));
  }
}
