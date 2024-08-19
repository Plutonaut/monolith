package com.game.engine.logic.concrete;

import com.game.engine.logic.AbstractLogic;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityInteractionController;
import com.game.engine.scene.entities.controllers.EntityTextController;
import com.game.engine.settings.EngineSettings;
import com.game.loaders.ini.INIFileModel;
import com.game.utils.application.values.ValueMap;
import com.game.utils.enums.*;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Sandbox3DLogic extends AbstractLogic {
  Entity diagnosticsTextEntity;
  Entity uiRefreshButtonEntity;
  EntityTextController uiTextController;
  EntityInteractionController uiInteractionController;

  public Sandbox3DLogic(EngineSettings settings) {
    super(settings);
  }

  void updateDiagnostics() {
    String diagnostics = scene.diagnostics().getPreviousRenderDiagnostics();
    diagnosticsTextEntity.redrawText(diagnostics);
  }

  void handleLeftMouseClick(int action) {
    if (action == GLFW.GLFW_PRESS && uiInteractionController.isHover()) updateDiagnostics();
  }

  void handleMouseInput(EGUIEvent event) {
    switch (event) {
      case EGUIEvent.ENTER -> uiTextController.setColor(Color.RED);
      case EGUIEvent.EXIT -> uiTextController.setColor();
    }
  }

  @Override
  protected String windowTitle() {
    return "3D Sandbox";
  }

  @Override
  public void onStart() {
    loadLights();

    Entity skyBox = scene
      .createSkyBox(EModel.BASIC_SKYBOX.name(), EModel.BASIC_SKYBOX.path())
      .scale(100f);
    INIFileModel terrainSettings = INIFileModel.load("properties/ini/proc_terrain_1.ini");
    String mossTerrainId = "mossTextureTerrain";
    ValueMap mossTerrainMap = terrainSettings.modelSection(mossTerrainId);
    Model mossTerrainModel = scene.model(mossTerrainMap);
    Entity mossTerrainEntity = scene.createEntity(
      mossTerrainId,
      mossTerrainModel,
      ERenderer.SCENE,
      EProjection.PERSPECTIVE,
      EGLParam.BLEND,
      EGLParam.CULL,
      EGLParam.DEPTH
    ).move(0, -1f, 0).scale(10);
    uiRefreshButtonEntity = scene.createText("refresh_button", "Click to Refresh").move2D(25f, 25f);
    uiInteractionController = uiRefreshButtonEntity.controllers().interaction();
    uiInteractionController.listen(this::handleMouseInput);
    uiTextController = uiRefreshButtonEntity.controllers().text();
    uiTextController.borderSize(20);
    diagnosticsTextEntity = scene.createText("diagnostics_text", "<N/A>").move2D(25f, 50f);
    diagnosticsTextEntity.controllers().text().setColor(Color.BLACK);

    scene.window().mouse().addListener(GLFW.GLFW_MOUSE_BUTTON_1, this::handleLeftMouseClick);
    scene.bind(mossTerrainEntity, skyBox, uiRefreshButtonEntity, diagnosticsTextEntity);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    uiInteractionController.onMouseInput(scene.window().mouse(), uiTextController.bounds());
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
  }
}
