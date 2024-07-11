package com.game.engine.scene;

import com.game.caches.GlobalCache;
import com.game.engine.EngineSettings;
import com.game.engine.audio.AudioManager;
import com.game.engine.render.mesh.definitions.MeshDefinition;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.PacketManager;
import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.projection.Projection;
import com.game.engine.window.Window;
import com.game.utils.engine.ModelResourceLoader;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Accessors(fluent = true)
@Data
public class Scene {
  private final Window window;
  private final Camera camera;
  private final Projection projection;
  private final AudioManager audio;
  private final LightingManager lighting;
  private final PacketManager packets;

  public Scene(EngineSettings settings, String name) {
    window = new Window(
      settings.width(),
      settings.height(),
      name,
      settings.debug(),
      true,
      settings.vsync()
    );
    camera = new Camera(
      new Vector3f(),
      new Vector3f(),
      settings.mouseSensitivity(),
      settings.movementSpeed()
    );
    projection = new Projection(settings.fov(), settings.zNear(), settings.zFar());

    audio = new AudioManager();
    lighting = new LightingManager();
    packets = new PacketManager();
  }

  public Scene load(ERenderer shader, String path, boolean animated) {
    return pipe(shader, ModelResourceLoader.load(path, animated));
  }

  public Scene pipe(ERenderer shader, String modelName, MeshDefinition meshDefinition) {
    return pipe(shader, meshDefinition.createModel(modelName));
  }

  public Scene pipe(ERenderer shader, Model model) {
    packets.bind(shader, model);
    return this;
  }

  public Matrix4f modelViewMat(Entity entity) {
    return projection.modelView(entity.transform().worldModelMat(), camera);
  }

  public Matrix4f modelOrthoMat(Entity entity) {
    return projectionMat(EProjection.ORTHOGRAPHIC).mul(entity.transform().model());
  }

  public Vector4f viewSpace(EProjection type, Vector4f point) {
    Vector4f viewPoint = new Vector4f(point).mul(projectionMat(type).invert());

    viewPoint.z = -1.0f;
    viewPoint.w = 0.0f;

    return viewPoint;
  }

  public Vector2f screenSpace(Vector3f point) {
    float x = (point.x + point.x * 0.5f) * window.width();
    float y = (point.y - point.y * 0.5f) * window.height();

    return new Vector2f(x, y);
  }

  public Matrix4f projectionMat(EProjection type) {
    return switch (type) {
      case FONT_ORTHOGRAPHIC -> projection.fontOrthographic(window);
      case ORTHOGRAPHIC -> projection.orthographic(window);
      case PERSPECTIVE -> projection.perspective(window);
    };
  }

  public void enter() {
    window().clear();
    window().viewport();
  }

  public void exit() {
    window().update();
    window().poll();
  }

  public void dispose() {
    GlobalCache.instance().dispose();
    audio.dispose();
    window.dispose();
  }
}
