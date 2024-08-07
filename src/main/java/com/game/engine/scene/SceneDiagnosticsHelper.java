package com.game.engine.scene;

import com.game.caches.GlobalCache;
import com.game.graphics.materials.Material;
import com.game.utils.engine.logging.DiagnosticLoggingHandler;
import com.game.utils.logging.LoggingUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class SceneDiagnosticsHelper {
  private final DiagnosticLoggingHandler diagnostics;
  private boolean enabled;

  public SceneDiagnosticsHelper(boolean enabled) {
    this.enabled = enabled;

    diagnostics = new DiagnosticLoggingHandler();
  }

  public void toggleDiagnostics(boolean enabled) {
    this.enabled = enabled;

    if (enabled) log.info("Scene diagnostics have been enabled.");
    else log.debug("Scene diagnostics have been disabled.");
  }

  public void capture(Scene scene) {
    if (!enabled) {
      log.debug("Scene diagnostics are disabled!");
      return;
    }

    diagnostics.init("Diagnostics");
    diagnostics
      .open("Scene")
      .row("Timestamp", LocalDateTime.now().format(LoggingUtils.LOG_DATE_TIME_FORMATTER))
      .close();
    diagnostics
      .open(scene.window().title())
      .row("width", scene.window().width())
      .row("height", scene.window().height())
      .close();
    diagnostics
      .open("Camera")
      .row("Position", scene.camera().position())
      .row("Rotation", scene.camera().rotation())
      .row("Mouse Sensitivity: ", scene.camera().mouseSensitivity())
      .row("Camera Speed: ", scene.camera().cameraSpeed())
      .close();
    diagnostics.open("Lighting");
    scene.lighting().lights().forEach((key, value) -> {
      diagnostics.row(key);
      diagnostics.row("Color", value.color());
      diagnostics.row("Factor", value.factor());
    });
    diagnostics.close();

    diagnostics.open("Entities");
    scene.entities().entities().forEach((key, value) -> {
      diagnostics
        .row(key, value.id())
        .row("Position", value.transform().position())
        .row("Rotation", value.transform().rotation())
        .row("Scale", value.transform().scale())
        .row("Shader", value.parameters().shader().key())
        .row("Projection", value.parameters().projection().name());
      diagnostics.row("Meshes");
      value.meshes().forEach((mesh) -> {
        diagnostics
          .row(mesh.key(), mesh.glId())
          .row("Vertex Count", mesh.vertexCount())
          .row("Is Complex", mesh.isComplex())
          .row("Min vertex", mesh.min())
          .row("Max vertex", mesh.max());
        Material material = mesh.material();
        diagnostics.row("Material", material.name());
        material.textures().pack().values().forEach(diagnostics::row);
        material.colors().colors().forEach(diagnostics::row);
        mesh
          .vaas()
          .forEach(v -> {
                     diagnostics
                       .row("Vertex Attribute Array")
                       .row("Stride", v.stride());
                     v.attributes().forEach(a -> diagnostics
                       .row("Attribute", a.key())
                       .row("Location", a.location())
                       .row("Size", a.size())
                       .row("Offset", a.offset())
                       .row("Divisor", a.divisor()));
                   }
          );
      });
    });
    GlobalCache.instance().computeDiagnostics(diagnostics);
    diagnostics.close();
    diagnostics.dispose();
  }
}
