package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.application.RandomNumberGenerator;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.*;
import com.game.utils.math.ScalarUtils;
import lombok.extern.slf4j.Slf4j;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

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
      .createObject(EModel.CUBE.name(), EModel.CUBE.path())
      .scale(0.25f)
      .move(0f, 0f, 1f);
    cubeEntityCopy = scene.createEntity(cubeEntity).scale(0.5f).move(1f, 0f, 1f);
    Entity skyboxEntity = scene.createSkyBox("skybox", EModel.BASIC_SKYBOX.path()).scale(100f);
    Entity terrainEntity = scene.createTerrain("proc_moss", 128).addPhysics().scale(10);

    Model instanceCubeModel = scene.cloneModel(EModel.CUBE.name(), "instanced");
    ValueStore store = new ValueStore();
    ValueStore colorStore = new ValueStore();
    RandomNumberGenerator rng = new RandomNumberGenerator(0);
    Matrix4f mat = new Matrix4f();
    int size = 4;
    int halfSize = 4 / 2;
    int instances = size * size;
    for (int i = -halfSize; i < halfSize; i++) {
      for (int j = -halfSize; j < halfSize; j++) {
        store.add(mat.identity().translate(i, 0, j).scale(0.25f));
        for (int k = 0; k < 3; k++) {
          float c = rng.nextf();
          colorStore.add(c);
        }
      }
    }

    instanceCubeModel.meshInfo().forEach(meshInfo -> {
      VertexInfo vertex = new VertexInfo(
        store.asArray(),
        EAttribute.IMX.getValue(),
        size,
        size,
        1,
        GL46.GL_DYNAMIC_DRAW
      );
      meshInfo.addVertices(vertex);
      vertex = new VertexInfo(
        colorStore.asArray(),
        EAttribute.CLR.getValue(),
        3,
        1,
        0,
        GL46.GL_DYNAMIC_DRAW
      );
      meshInfo.addVertices(vertex);
      meshInfo.instances(instances);
    });
    Entity instanceCube = scene.createEntity(
      "instance_cube",
      instanceCubeModel,
      ERenderer.PARTICLE,
      EProjection.PERSPECTIVE,
      EGLParam.CULL,
      EGLParam.DEPTH,
      EGLParam.BLEND
    );


    musicSource = scene.audio("Creepy music", EAudio.MUSIC_WOO.value());
    scene.bind(cubeEntity, cubeEntityCopy, skyboxEntity, terrainEntity, instanceCube);

    scene.window().keyboard().onKeyPress(GLFW.GLFW_KEY_M, (action) -> {
      if (action == GLFW.GLFW_PRESS) {
        log.info("M pressed!");
        if (musicSource.isPlaying()) musicSource.stop();
        else musicSource.play();
      }
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
