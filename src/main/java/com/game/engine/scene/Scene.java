package com.game.engine.scene;

import com.game.caches.GlobalCache;
import com.game.engine.EngineSettings;
import com.game.engine.audio.AudioManager;
import com.game.engine.render.mesh.definitions.MeshDefinition;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.RenderPacket;
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
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

@Accessors(fluent = true)
@Data
public class Scene {
  private final Window window;
  private final Camera camera;
  private final Projection projection;
  private final AudioManager audio;
  private final LightingManager lighting;
//  private final EntityManager entities;
  private final HashMap<ERenderer, RenderPacket> packets;

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
    projection = new Projection();

//    entities = new EntityManager();
    audio = new AudioManager();
    lighting = new LightingManager();
    packets = new HashMap<>();
  }

  public Scene load(ERenderer shader, String path, boolean animated) {
    return bind(shader, ModelResourceLoader.load(path, animated));
  }

  public Scene bind(ERenderer shader, String modelName, MeshDefinition meshDefinition) {
    return bind(shader, meshDefinition.createModel(modelName));
  }

  public Scene bind(ERenderer shader, Model model) {
    packet(shader).queue(model);
    return this;
  }

  public RenderPacket packet(ERenderer shader) {
    return packets.computeIfAbsent(shader, RenderPacket::new);
  }

  public void addPacket(RenderPacket packet) {
    packets.put(packet.destination(), packet);
  }

  public ArrayBlockingQueue<Entity> renderQueue(ERenderer renderer) {
    return packets.get(renderer).renderQueue();
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
