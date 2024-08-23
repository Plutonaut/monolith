package com.game.engine.scene.diagnostics;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.Material;
import com.game.utils.engine.logging.DiagnosticLoggingHandler;
import com.game.utils.logging.LoggingUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Accessors(fluent = true)
@Slf4j
public class SceneDiagnosticsHelper {
  private final DiagnosticLoggingHandler diagnostics;
  @Getter
  private final ArrayList<String> boundEntityNames;
  private StringBuilder renderDiagnostics;
  private boolean enabled;

  public SceneDiagnosticsHelper(boolean enabled) {
    toggleDiagnostics(enabled);
    diagnostics = new DiagnosticLoggingHandler();
    boundEntityNames = new ArrayList<>();
  }

  public void onRenderBegin() {
    renderDiagnostics = new StringBuilder("Scene Diagnostics:\n");
    boundEntityNames.clear();
  }

  public void entityRendered(Entity entity) {
    boundEntityNames.add(entity.name());
    renderDiagnostics
      .append("Entity: ")
      .append(entity.name())
      .append(" Mesh Count: ")
      .append(entity.meshes().size())
      .append(" Total Vertex Count: ")
      .append(entity.meshes().stream().mapToInt(Mesh::vertexCount).reduce(Integer::sum).orElse(0))
      .append("\n");
  }

  public String getPreviousRenderDiagnostics() {
    return renderDiagnostics.toString();
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

    diagnostics.init("Scene Diagnostics");
    diagnostics.open("Scene").row(
      "Timestamp",
      LocalDateTime.now().format(LoggingUtils.LOG_DATE_TIME_FORMATTER)
    ).close();
    diagnostics.open(scene.window().title()).row("width", scene.window().width()).row(
      "height",
      scene
        .window()
        .height()
    ).close();
    diagnostics.open("Camera").row("Position", scene.camera().position()).row(
      "Rotation",
      scene
        .camera()
        .rotation()
    ).row("Mouse Sensitivity: ", scene.camera().mouseSensitivity()).row(
      "Camera Speed: ",
      scene.camera().cameraSpeed()
    ).close();
    diagnostics.open("Lighting");
    scene.lighting().lights().forEach((key, value) -> {
      diagnostics.row(key);
      diagnostics.row("Color", value.color());
      diagnostics.row("Factor", value.factor());
    });

    diagnostics.open("Entities");
    scene.entities().entities().forEach((key, value) -> {
      diagnostics
        .emdash()
        .row(key, value.id())
        .emdash()
        .row("Position", value.transform().position())
        .row(
          "Rotation",
          value
            .transform()
            .rotation()
        )
        .row("Scale", value.transform().scale())
        .row("Shader", value.parameters().shader().key())
        .row("Projection", value.parameters().projection().name());
      diagnostics.row("Meshes");
      value.meshes().forEach((mesh) -> {
        diagnostics
          .emdash()
          .row(mesh.key(), mesh.glId())
          .row("Vertex Count", mesh.vertexCount())
          .emdash()
          .row(
            "Is Complex",
            mesh.isComplex()
          )
          .row(mesh.bounds().toString());
        Material material = mesh.material();
        diagnostics.row("Material", material.name());
        material.textures().pack().values().forEach(diagnostics::row);
        material.colors().colors().forEach(diagnostics::row);
        mesh.vaas().forEach(v -> {
          diagnostics.row("Vertex Attribute Array").row("Stride", v.stride());
          v.attributes().forEach(a -> diagnostics.row("Attribute", a.key()).row(
            "Location",
            a.location()
          ).row("Size", a.size()).row("Offset", a.offset()).row("Divisor", a.divisor()));
        });
      });
    });

    diagnostics
      .open("Active Terrain Chunks")
      .row("Max LOD", scene.terrain().maxLOD())
      .row("Min LOD", scene.terrain().minLOD())
      .row("Number of chunks visible", scene.terrain().numChunksVisible());
    scene.activeTerrainChunks().forEach((activeChunk) -> {
      diagnostics
        .emdash()
        .row("Terrain Chunk", activeChunk.coordinates().toString())
        .emdash()
        .row(activeChunk.bounds().toString())
        .row("LOD", activeChunk.lod())
        .row("LOD Index", activeChunk.lodIndex());
    });
    GlobalCache.instance().computeDiagnostics(diagnostics);
    diagnostics.close();
    diagnostics.dispose();
  }
}
