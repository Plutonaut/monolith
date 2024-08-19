package com.game.engine.logic.concrete;

import com.game.engine.controls.Keyboard;
import com.game.engine.logic.AbstractLogic;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.lighting.lights.Light;
import com.game.engine.settings.EngineSettings;
import com.game.utils.application.LambdaCounter;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.*;
import com.game.utils.logging.PrettifyUtils;
import lombok.extern.slf4j.Slf4j;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

@Slf4j
public class TestProceduralGenerationLogic extends AbstractLogic {
  static final String FPS_HUD_TEXT = "FPS: %d";
  static final String NO_LIGHT_SELECTED_TEXT = "<Click Left or Right to select a light>";
  static final String LIGHT_SELECTED_TEXT = "%s Light | Factor %s";
  static final String NO_ENTITY_SELECTED_TEXT = "<Left click to select an entity>";
  //  Entity cubeEntity;
  Entity hudEntity;
  Entity lightingTextEntity;
  AudioSource musicSource;
  Light currentLight;
  Vector4f currentLightColor;
  String currentLightKey;
  int currentLightInc = 0;
  Keyboard kb;
//  float tick = 0;

  public TestProceduralGenerationLogic(EngineSettings settings) {
    super(settings);
    currentLightColor = new Vector4f();
  }

  void onLightFactorChange(float inc) {
    if (currentLight != null) {
      float updatedFactor = Math.clamp(currentLight.factor() + inc, 0, 1);
      currentLight.factor(updatedFactor);
      updateText();
    }
  }

  void updateText(String lightKey) {
    currentLightKey = lightKey;
    updateText();
  }

  void updateText() {
    float factor = currentLight != null ? currentLight.factor() : 1f;
    String lightText = currentLight != null ? LIGHT_SELECTED_TEXT.formatted(
      currentLightKey,
      PrettifyUtils.prettify(factor)
    ) : NO_LIGHT_SELECTED_TEXT;
    currentLightColor.set(factor);
    lightingTextEntity.controllers().text().setColor(currentLightColor);
    lightingTextEntity.redrawText(lightText);
  }

  void onLightSelection(int inc) {
    currentLightInc = Math.clamp(currentLightInc + inc, 0, 5);
    currentLight = switch (currentLightInc) {
      case 1 -> scene.lighting().directionalLight();
      case 2 -> scene.lighting().pointLight("test_A");
      case 3 -> scene.lighting().pointLight("test_B");
      case 4 -> scene.lighting().spotLight("test_A");
      case 5 -> scene.lighting().spotLight("test_B");
      default -> null;
    };
    switch (currentLightInc) {
      case 0 -> updateText();
      case 1 -> updateText("Directional");
      case 2 -> updateText("Point A");
      case 3 -> updateText("Point B");
      case 4 -> updateText("Spot A");
      case 5 -> updateText("Spot B");
    }
  }

  @Override
  protected String windowTitle() {
    return "Test Procedural Generation";
  }

  @Override
  public void onStart() {
    loadLights();
    musicSource = scene.audio("Creepy music", EAudio.MUSIC_WOO.value());
    hudEntity = scene.createText("hud_fps", FPS_HUD_TEXT.formatted(currentFPS)).move2D(20, 25);
    Entity skyboxEntity = scene.createSkyBox("skybox", EModel.BASIC_SKYBOX.path()).scale(100f);
/*
        Entity terrainEntity = scene.createTerrain("proc_moss", 128).addPhysics().scale(10);
     TODO: If model does not exist, this currently fails once it reaches the ObjectResourceModelGenerator class.
      Need to error out before then.
    */

    Model instanceCubeModel = scene.model(EModel.CUBE.name(), EModel.CUBE.path());
    ValueStore store = new ValueStore();
    Matrix4f mat = new Matrix4f();
    int size = 4;
    LambdaCounter counter = new LambdaCounter();
    for (int i = -size; i < size; i++) {
      for (int j = -size; j < size; j++) {
        for (int k = 0; k < size * 2; k++) {
          store.add(mat.identity().translate(i, k, j).scale(0.5f));
          counter.inc();
        }
      }
    }
    int instanceCount = counter.value();
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
      meshInfo.instances(instanceCount);
    });
    Entity instanceCube = scene.createEntity(
      "instance_cube",
      instanceCubeModel,
      ERenderer.SCENE,
      EProjection.PERSPECTIVE,
      EGLParam.CULL,
      EGLParam.DEPTH,
      EGLParam.BLEND
    );

    Entity instanceTextEntity = scene.createText(
      "instance_text",
      "Instance count: %d".formatted(instanceCount)
    ).move2D(25, 75);

    lightingTextEntity = scene.createText("lighting_text", NO_LIGHT_SELECTED_TEXT).move2D(25, 125);
    scene.bind(instanceTextEntity, skyboxEntity, instanceCube, hudEntity, lightingTextEntity);

    kb = (Keyboard) scene.window().keyboard().addListener(GLFW.GLFW_KEY_M, (action) -> {
      if (action == GLFW.GLFW_PRESS) {
        log.info("M pressed!");
        if (musicSource.isPlaying()) musicSource.stop();
        else musicSource.play();
      }
    }).addListener(GLFW.GLFW_KEY_LEFT, (action) -> {
      if (action == GLFW.GLFW_PRESS) onLightSelection(-1);
    }).addListener(GLFW.GLFW_KEY_RIGHT, (action) -> {
      if (action == GLFW.GLFW_PRESS) onLightSelection(1);
    });
  }

  @Override
  public void input() {
    captureCameraMovementInput();

    if (scene.window().isKeyPressed(GLFW.GLFW_KEY_UP)) onLightFactorChange(0.01f);
    else if (scene.window().isKeyPressed(GLFW.GLFW_KEY_DOWN)) onLightFactorChange(-0.01f);
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
    hudEntity.redrawText(FPS_HUD_TEXT.formatted(currentFPS));
//    Vector3f cubePosition = cubeEntity.transform().position();
//    tick += 0.025f;
//    cubePosition.y = ScalarUtils.lerp(0.5f, 1.0f, (float) Math.sin(tick));
  }
}
