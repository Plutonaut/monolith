package com.game.engine.scene.hud;

import com.game.caches.GlobalCache;
import com.game.engine.physics.Bounds2D;
import com.game.engine.physics.Hit;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.controllers.EntityInteractionController;
import com.game.engine.scene.entities.controllers.EntityTextController;
import com.game.engine.settings.EngineSettings;
import com.game.graphics.shaders.Program;
import com.game.utils.engine.ColorUtils;
import com.game.utils.engine.TextureUtils;
import com.game.utils.enums.EGUIEvent;
import com.game.utils.enums.EMaterialColor;
import com.game.utils.enums.ERenderer;
import com.game.utils.logging.PrettifyUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.HashMap;

@Accessors(fluent = true)
public class Hud {
  private static final String TOG = "toggle";
  private static final String DIA = "diagnostics";
  private static final String SEL = "selected";
  private static final String CAM = "camera";
  private static final String CTR = "center";
  private static final String SEL_DFLT_TEXT = "<Please reselect an entity>";

  private final HashMap<String, Entity> hudEntityMap;
  private final Bounds2D toggleDiagnosticsButtonBounds;
  private final Scene scene;

  private EntityTextController toggleDiagnosticsTextController;
  private EntityInteractionController toggleDiagnosticsInteractionController;
  private Mesh toggleDiagnosticsBGMesh;

  private boolean diagnosticsVisible;
  @Getter
  @Setter
  private float border;
  @Getter
  @Setter
  private Vector4f color;
  @Getter
  @Setter
  private Vector4f hoverColor;
  @Getter
  @Setter
  private float textScale;

  public Hud(Scene scene, EngineSettings settings) {
    this.scene = scene;
    this.border = settings.hudBorder();
    this.textScale = settings.hudTextScale();
    this.color = ColorUtils.normalize(settings.hudTextColor());
    this.hoverColor = ColorUtils.convert(Color.decode("#038C8C"));
    this.toggleDiagnosticsButtonBounds = new Bounds2D();
    hudEntityMap = new HashMap<>();
  }

  public void onStart() {
    Entity toggleDiagnosticsButtonEntity = redrawEntity(
      TOG,
      currentDiagnosticsButtonLabel(),
      border,
      border
    );
    toggleDiagnosticsTextController = toggleDiagnosticsButtonEntity.controllers().text();
    toggleDiagnosticsInteractionController = toggleDiagnosticsButtonEntity
      .controllers()
      .interaction();
    toggleDiagnosticsInteractionController.listen(this::handleMouseInput);
    toggleDiagnosticsTextController.setColor(color);

    Bounds2D bounds = toggleDiagnosticsTextController.bounds();
    MeshInfo backgroundMeshInfo = new Quad(
      "hud_toggle_bg",
      new Vector2f(border, border),
      bounds.size().x,
      bounds.size().y
    )
      .diffuseTexture(TextureUtils.GUI_BG)
      .diffuseColor(ColorUtils.convert(Color.white))
      .createMeshInfo();
    Program program = GlobalCache.instance().program(ERenderer.FONT.key());
    toggleDiagnosticsBGMesh = GlobalCache.instance().mesh(
      backgroundMeshInfo.name(),
      (name) -> backgroundMeshInfo.create(program)
    );
    toggleDiagnosticsButtonBounds.set(toggleDiagnosticsBGMesh.bounds().boundsXY());
    toggleDiagnosticsButtonBounds.origin().set(border);
    toggleDiagnosticsButtonEntity.addMesh(toggleDiagnosticsBGMesh);
    scene.bind(toggleDiagnosticsButtonEntity);
    scene.window().mouse().addListener(GLFW.GLFW_MOUSE_BUTTON_1, this::handleLeftMouseClick);
  }

  public void updateCenterText(String text) {
    if (text != null) redrawEntity(
      CTR,
      text,
      scene.window().width() / 2f,
      scene.window().height() / 2f
    );
  }

  public void showCenterText() {
    scene.bind(ERenderer.FONT, CTR);
  }

  public void hideCenterText() {
    scene.unbind(ERenderer.FONT, CTR);
  }

  public void onInput() {
    toggleDiagnosticsInteractionController.onMouseInput(
      scene.window().mouse(),
      toggleDiagnosticsButtonBounds
    );
    if (diagnosticsVisible) redrawText(CAM, scene.camera().toString());
  }

  public void handleLeftMouseClick(int action) {
    if (action == GLFW.GLFW_PRESS) {
      if (toggleDiagnosticsInteractionController.isHover()) onDiagnosticsToggled();
      else scene.rayCastMouseClick(false, this::onHit);
    }
  }

  // TODO: Abstract this out into an interface that gets called directly by the Mouse input device.
  void onMouseOver(Vector4f c) {
    toggleDiagnosticsTextController.setColor(c);
    toggleDiagnosticsBGMesh.material().color(EMaterialColor.DIF.getValue(), c);
  }

  void handleMouseInput(EGUIEvent event) {
    if (toggleDiagnosticsTextController == null) return;

    switch (event) {
      case EGUIEvent.ENTER -> onMouseOver(hoverColor);
      case EGUIEvent.EXIT -> onMouseOver(color);
    }
  }

  void onHit(Hit hit) {
    Entity entity = hit.entity();
    scene.boundEntities().forEach(e -> e.onMeshSelected(hit.meshId()));
    if (diagnosticsVisible) {
      if (entity == null) redrawText(SEL, SEL_DFLT_TEXT);
      else {
        Mesh m = entity.meshByGlID(hit.meshId());
        String builder = "Hit: " + PrettifyUtils.prettify(hit
                                                            .ray()
                                                            .result()) + "\nEntity: " + entity.name() + "\nposition: " + PrettifyUtils.prettify(
          entity
            .transform()
            .position()) + "\nscale: " + entity.transform().scale + "\nmesh: " + m.name() + "\n\tglId: " + m.glId() + "\n\tvertex count: " + m.vertexCount();
        redrawText(SEL, builder);
      }
    }
  }

  void redrawText(String id, String text) {
    hudEntityMap.get(id).redrawText(text);
  }

  Entity redrawEntity(String id, String text, float x, float y) {
    return hudEntityMap.computeIfAbsent(id, entityId -> {
      Entity e = scene.createText(id, text).scale(textScale).move2D(x, y);
      e.controllers().text().setColor(color);
      return e;
    }).redrawText(text); // won't run if absent since text hasn't changed.
  }

  String currentDiagnosticsButtonLabel() {
    String toggleButtonText = diagnosticsVisible ? "Hide" : "Show";
    return toggleButtonText + " Diagnostics";
  }

  void onDiagnosticsToggled() {
    diagnosticsVisible = !diagnosticsVisible;
    redrawText(TOG, currentDiagnosticsButtonLabel());
    if (diagnosticsVisible) {
      redrawEntity(DIA, scene.diagnostics().getPreviousRenderDiagnostics(), border, border + 50f);
      redrawEntity(CAM, scene.camera().toString(), border, scene.window().height() - (border * 4));
      redrawEntity(
        SEL,
        SEL_DFLT_TEXT,
        scene.window().width() - (scene.window().width() / 4f),
        border
      );
      scene.bind(ERenderer.FONT, SEL, CAM, DIA);
    } else {
      scene.unbind(ERenderer.FONT, SEL, CAM, DIA);
    }
  }
}
