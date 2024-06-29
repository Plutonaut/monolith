package com.game.engine.scene;

import com.game.caches.GlobalCache;
import com.game.engine.audio.AudioManager;
import com.game.engine.render.IRenderable;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.EntityManager;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.projection.Projection;
import com.game.engine.window.Window;
import com.game.engine.scene.camera.Camera;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.concurrent.ArrayBlockingQueue;

@Accessors(fluent = true)
@Data
public class Scene {
  public static final int MAX_QUEUE_SIZE = 32;
  private final Window window;
  private final Camera camera;
  private final Projection projection;
  private final AudioManager audio;
  private final LightingManager lighting;
  private final EntityManager entities;

  public Scene(int width, int height, String name) {
    window = new Window(width, height, name, true, true, false);
    camera = new Camera(new Vector3f(), new Vector3f(), 0.2f, 0.05f);
    projection = new Projection();

    entities = new EntityManager();
    audio = new AudioManager();
    lighting = new LightingManager();
  }

  public ArrayBlockingQueue<Entity> subQueue(ERenderer renderer) {
    ArrayBlockingQueue<Entity> queue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE, true);
    this.entities.renderStream().filter(item -> matches(item, renderer)).forEach(queue::add);

    return queue;
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

  boolean matches(IRenderable item, ERenderer type) {
    return item.shaders().stream().anyMatch(shader -> shader == type);
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
