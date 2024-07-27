package com.game.engine.scene;

import com.game.caches.GlobalCache;
import com.game.engine.audio.AudioManager;
import com.game.engine.physics.Hit;
import com.game.engine.physics.IHitListener;
import com.game.engine.physics.Ray;
import com.game.engine.render.mesh.definitions.MeshDefinition;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.PacketManager;
import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.TextEntity;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.projection.Projection;
import com.game.engine.scene.terrain.procedural.ProceduralNoiseData;
import com.game.engine.scene.terrain.procedural.ProceduralTerrainGenerationData;
import com.game.engine.scene.terrain.procedural.ProceduralTerrainGenerator;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.graphics.texture.TextureMapData;
import com.game.utils.engine.loaders.FontResourceLoader;
import com.game.utils.engine.loaders.ModelResourceLoader;
import com.game.utils.engine.loaders.SpriteResourceLoader;
import com.game.utils.enums.EModel;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.ESprite;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.*;
import org.lwjgl.opengl.GL46;

import java.awt.*;
import java.util.List;

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
      settings.cameraPosition(),
      new Vector3f(),
      settings.mouseSensitivity(),
      settings.movementSpeed()
    );
    projection = new Projection(settings.fov(), settings.zNear(), settings.zFar());

    audio = new AudioManager();
    lighting = new LightingManager();
    packets = new PacketManager();
  }

  // Support for culling back faces
  public Scene cull(boolean enabled) {
    GL46.glCullFace(GL46.GL_BACK);
    return toggleGl(GL46.GL_CULL_FACE, enabled);
  }

  public Scene wireframe(boolean enabled) {
    int mode = enabled ? GL46.GL_LINE : GL46.GL_FILL;
    GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, mode);
    return this;
  }

  public Scene depth(boolean enabled) {
    return toggleGl(GL46.GL_DEPTH_TEST, enabled);
  }

  // Support for transparencies
  public Scene blend(boolean enabled) {
    toggleGl(GL46.GL_BLEND, enabled);
    GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
    return this;
  }

  public Scene clearColor(float red, float green, float blue, float alpha) {
    GL46.glClearColor(red, green, blue, alpha);
    return this;
  }

  private Scene toggleGl(int target, boolean toggle) {
    if (toggle) GL46.glEnable(target);
    else GL46.glDisable(target);
    return this;
  }

  public Scene generateProceduralTerrain(
    String id, TextureMapData textureMapData
  ) {
    return generateProceduralTerrain(ERenderer.SCENE, id, textureMapData);
  }

  public Scene generateProceduralTerrain(ERenderer shader, String id, TextureMapData data) {
    return generateProceduralTerrain(shader, id, 64, 64, data, 1);
  }

  public Scene generateProceduralTerrain(
    ERenderer shader,
    String id,
    int width,
    int height,
    TextureMapData textureData,
    float scale
  ) {
    ProceduralNoiseData noise = new ProceduralNoiseData(
      new Vector2f(0f),
      0.25f,
      5,
      scale,
      2,
      0,
      false
    );
    ProceduralTerrainGenerationData data = new ProceduralTerrainGenerationData(
      id,
      width,
      height,
      0f,
      0.1f, textureData,
      noise
    );
    return generateProceduralTerrain(shader, data);
  }

  public Scene generateProceduralTerrain(ERenderer shader, ProceduralTerrainGenerationData data) {
    pipe(shader, ProceduralTerrainGenerator.generate(data));
    return this;
  }

  public Scene loadSkyBox3D(EModel model) {
    return loadSkyBox3D(model.name(), model.path());
  }

  public Scene loadSkyBox3D(String path) {
    return loadSkyBox3D(path, path);
  }

  public Scene loadSkyBox3D(String name, String path) {
    return load3D(ERenderer.SKYBOX, name, path, false);
  }

  public Scene loadText(String name, String text) {
    return loadText(name, text, false);
  }

  public Scene loadText(String name, String text, boolean antiAlias) {
    return loadText(name, text, antiAlias, GlobalCache.instance().getFont("Arial", 20));
  }

  public Scene loadText(String name, String text, boolean antiAlias, Font font) {
    return loadText(name, text, antiAlias, font, Color.white);
  }

  public Scene loadText(String name, String text, boolean antiAlias, Font font, Color color) {
    return pipe(ERenderer.FONT, FontResourceLoader.load(name, text, font, color, antiAlias));
  }

  public Scene load2D(ESprite sprite) {
    return load2D(sprite.path(), sprite.animated());
  }

  public Scene load2D(String path, boolean animated) {
    return load2D(ERenderer.SPRITE, path, animated);
  }

  public Scene load2D(ERenderer shader, String path, boolean animated) {
    return pipe(shader, SpriteResourceLoader.load(path, animated));
  }

  public Scene load3D(EModel model) {
    return load3D(ERenderer.MESH, model);
  }

  public Scene load3D(ERenderer shader, EModel model) {
    return load3D(shader, model.name(), model.path(), model.animated());
  }

  public Scene load3D(ERenderer shader, String path, boolean animated) {
    return load3D(shader, path, path, animated);
  }

  public Scene load3D(ERenderer shader, String name, String path, boolean animated) {
    return pipe(shader, ModelResourceLoader.load(name, path, animated));
  }

  public Scene pipe(ERenderer shader, MeshDefinition meshDefinition) {
    return pipe(shader, meshDefinition.createModel());
  }

  public Scene pipe(ERenderer shader, Model model) {
    packets.bind(shader, model);
    return this;
  }

  public Entity entity(String name) {
    return packets.getEntity(name);
  }

  public TextEntity gameText(String name) {
    return packets.getGameText(name);
  }

  public void rayCastMouseClickAll(IHitListener listener) {
    rayCastMouseClick(listener, true);
  }

  public void rayCastMouseClickClosest(IHitListener listener) {
    rayCastMouseClick(listener, false);
  }

  void rayCastMouseClick(IHitListener listener, boolean all) {
    Vector2f mousePosition = window.mouse().position();
    Vector3f normalizedDeviceSpace = window.normalizedDeviceSpace(mousePosition);

    Hit hit = new Hit();
    float closestDistance = Float.POSITIVE_INFINITY;
    List<Entity> entityStream = packets.getEntities();
    Vector2f result = new Vector2f();

    for (Entity entity : entityStream) {
      if (!entity.controllers().hasPhysics()) continue;

      Vector4f viewSpace = viewSpace(EProjection.PERSPECTIVE, normalizedDeviceSpace);
      Vector4f worldSpace = camera.worldPosition(viewSpace);
      Vector3f dir = new Vector3f(worldSpace.x, worldSpace.y, worldSpace.z);

      Ray ray = new Ray(camera.position(), dir, result);
      boolean intersects = entity.intersects(ray);
      if (intersects) {
        hit.ray(ray);
        if (all) {
          hit.entity(entity);
          listener.onHit(hit);
        } else if(ray.result().x < closestDistance) {
          hit.entity(entity);
          closestDistance = ray.result().x;
        }
      }
    }

    if (!all) {
      listener.onHit(hit);
    }
  }

  public Matrix4f modelViewMat3D(Entity entity) {
    return modelWorldSpaceMat(entity, camera.view3D());
  }

  public Matrix4f modelViewMat2D(Entity entity) {
    return modelWorldSpaceMat(entity, camera.view2D());
  }

  public Matrix4f modelProjectionMat(Entity entity, EProjection projection) {
    return modelWorldSpaceMat(entity, projectionMat(projection));
  }

  Matrix4f modelWorldSpaceMat(Entity entity, Matrix4f mat) {
    return new Matrix4f().set(mat).mul(entity.transform().worldModelMat());
  }

  public Vector4f viewSpace(EProjection type, Vector3f point) {
    return viewSpace(type, new Vector4f(point, 1.0f));
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
      case ORTHOGRAPHIC_CENTER_2D -> projection.centerOrthographic2D(window);
      case ORTHOGRAPHIC_FONT_2D -> projection.fontOrthographic2D(window);
      case ORTHOGRAPHIC_2D -> projection.orthographic2D(window);
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
