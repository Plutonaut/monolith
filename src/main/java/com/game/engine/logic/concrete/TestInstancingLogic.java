package com.game.engine.logic.concrete;

import com.game.caches.GlobalCache;
import com.game.engine.logic.AbstractLogic;
import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityTextController;
import com.game.engine.settings.EngineSettings;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;

@Slf4j
public class TestInstancingLogic extends AbstractLogic {
  Entity uiEntity;
  EntityTextController textController;

  public TestInstancingLogic(EngineSettings settings) {
    super(settings);
  }

  void onClick(EGUIEvent event) {
    log.info("On Click event {}", event);

    if (event == EGUIEvent.ENTER) uiEntity.controllers().text().setColor(Color.RED);
    else if (event == EGUIEvent.EXIT) uiEntity.controllers().text().setColor();
  }


  @Override
  protected String windowTitle() {
    return "Instance Render Test";
  }

  @Override
  public void onStart() {
    uiEntity = scene.createText("create_entity", "Create New Entity").move2D(25f, 25f);
    uiEntity.controllers().interaction().listen(this::onClick);
    textController = uiEntity.controllers().text();
    textController.borderSize(20);

    Material backgroundMaterial = new Material("bg_material");
    backgroundMaterial.texture(EMaterialTexture.DIF.value(),
                               "src/main/resources/textures/gui/gui_bg_01.png"
    );
    backgroundMaterial.color(EMaterialColor.DIF.getValue(), new Vector4f(1));

    MeshInfo info = new Quad(new Vector2f(textController.borderSize()),
                             textController.bounds().size().x(),
                             textController.bounds().size().y()
    ).meshInfo();
    VertexInfo positionVI = info.getVerticesByAttribute(EAttribute.POS);

    Mesh backgroundMesh = new Mesh("bg_mesh");
    backgroundMesh.vertexCount(info.vertexCount());
    backgroundMesh.material(backgroundMaterial);
    backgroundMesh.isComplex(true);

    Bounds3D bounds = MeshInfoUtils.calculateBounds(positionVI);
    backgroundMesh.updateBounds(bounds.min(), bounds.max());

    Program program = GlobalCache.instance().program(ERenderer.FONT.key());
    backgroundMesh.redrawAttributes(info, program);

    uiEntity.addMesh(backgroundMesh);

    scene.bind(uiEntity);
  }

  @Override
  public void input() {
    captureCameraMovementInput();
    uiEntity.controllers().interaction().onMouseInput(
      scene.window().mouse(),
      textController.bounds()
    );
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
  }
}
