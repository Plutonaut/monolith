package com.game.engine.scene;

import com.game.caches.GlobalCache;
import com.game.engine.audio.AudioManager;
import com.game.engine.physics.IHitListener;
import com.game.engine.physics.Raycaster;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.PacketManager;
import com.game.engine.scene.audio.AudioSource;
import com.game.engine.scene.camera.Camera;
import com.game.engine.scene.entities.Entity;
import com.game.engine.scene.entities.EntityManager;
import com.game.engine.scene.entities.EntityRenderParameters;
import com.game.engine.scene.generators.ModelGenerator;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.projection.Projection;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.utils.application.values.ValueMap;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.engine.TextureUtils;
import com.game.utils.enums.EFont;
import com.game.utils.enums.EGLParam;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
  private final EntityManager entities;
  private final ModelGenerator models;
  private final GLParameters parameters;
  private final PacketManager packets;
  private final SceneDiagnosticsHelper diagnostics;

  public Scene(EngineSettings settings, String name) {
    window = new Window(settings.width(),
                        settings.height(),
                        name,
                        settings.debug(),
                        true,
                        settings.vsync()
    );
    camera = new Camera(settings.cameraPosition(),
                        new Vector3f(),
                        settings.mouseSensitivity(),
                        settings.movementSpeed()
    );
    projection = new Projection(settings.fov(), settings.zNear(), settings.zFar());

    audio = new AudioManager();
    lighting = new LightingManager();
    packets = new PacketManager();
    parameters = new GLParameters();
    entities = new EntityManager();
    models = new ModelGenerator();
    diagnostics = new SceneDiagnosticsHelper(settings.diagnostics());
  }

  public AudioSource audio(String key, String path) {
    return audio.load(key, path);
  }

  public Model model(String id) {
    return model(id, "");
  }

  public Model model(String id, String path) {
    return model(id, path, ""); // default: "object"
  }

  public Model model(String id, String path, String type) {
    return model(models.builder().id(id).path(path).type(type).build());
  }

  public Model model(ValueMap map) {
    return GlobalCache.instance().model(map.get("id"), modelId -> models.generate(map));
  }

  public Model cloneModel(String modelId, String cloneId) {
    Model model = model(modelId);
    String copyId = modelId + "_" + cloneId;
    List<String> meshInfo = model
      .meshInfo()
      .stream()
      .map((d) -> GlobalCache
        .instance()
        .meshInfo(cloneId, n -> MeshInfoUtils.clone(d, cloneId))
        .name())
      .toList();

    return GlobalCache.instance().model(copyId, (id) -> {
      Model copy = new Model(id);
      copy.meshData().addAll(meshInfo);
      copy.animations().addAll(model.animations());
      return copy;
    });
  }

  public Entity entity(String id) {
    return entities.get(id);
  }

  public Entity createObject(String id, String path) {
    ValueMap map = models.builder().id(id).path(path).animated(false).build();
    Model model = model(map);
    return createEntity(id,
                        model,
                        ERenderer.SCENE,
                        EProjection.PERSPECTIVE,
                        EGLParam.BLEND,
                        EGLParam.DEPTH,
                        EGLParam.CULL
    );
  }

  public Entity createText(String id, String text) {
    ValueMap map = models
      .builder()
      .id(id)
      .text(text)
      .antiAlias(true)
      .diffuseColor(Color.white)
      .asText()
      .fontName(EFont.FESTER.value())
      .fontSize(24)
      .fontColor(Color.white)
      .asSprite()
      .build();
    Model model = model(map);
    return createEntity(id,
                        model,
                        ERenderer.FONT,
                        EProjection.ORTHOGRAPHIC_FONT_2D,
                        EGLParam.BLEND,
                        EGLParam.DEPTH
    );
  }

  public Entity createSprite(String id, String path) {
    ValueMap map = models
      .builder()
      .id(id)
      .path(path)
      .animated(false)
      .clamped(false)
      .useAtlas()
      .asSprite()
      .build();
    Model model = model(map);
    return createEntity(id,
                        model,
                        ERenderer.SPRITE,
                        EProjection.ORTHOGRAPHIC_FONT_2D,
                        EGLParam.BLEND,
                        EGLParam.DEPTH
    );
  }

  public Entity createSkyBox(String id, String path) {
    ValueMap map = models.builder().id(id).path(path).animated(false).build();
    Model model = model(map);
    return createEntity(id,
                        model,
                        ERenderer.SKYBOX,
                        EProjection.PERSPECTIVE,
                        EGLParam.BLEND,
                        EGLParam.DEPTH
    );
  }

  public Entity createTerrain(String id, int size) {
    ValueMap map = models
      .builder()
      .id(id)
      .width(size)
      .height(size)
      .minVertexHeight(0)
      .maxVertexHeight(0.25f)
      .asTerrain()
      .heightMapTexturePath(TextureUtils.MOSS_HGHT_TEXTURE_PATH)
      .diffuseTexturePath(TextureUtils.MOSS_DIFF_TEXTURE_PATH)
      .build();
    Model model = model(map);
    return createEntity(id,
                        model,
                        ERenderer.SCENE,
                        EProjection.PERSPECTIVE,
                        EGLParam.BLEND,
                        EGLParam.CULL,
                        EGLParam.DEPTH
    );
  }

  public Entity createEntity(
    String id, Model model, ERenderer shader, EProjection projection, EGLParam... params
  ) {
    EntityRenderParameters parameters = new EntityRenderParameters(shader,
                                                                   projection,
                                                                   EGLParam.bitmap(params)
    );
    return createEntity(id, model, parameters);
  }

  // TODO: Remove fromCache, change mesh loading strategy.
  public Entity createEntity(String id, Model model, EntityRenderParameters parameters) {
    return entities.create(id, model, parameters, false);
  }

  public Entity createEntity(Entity entity) {
    return entities.createFrom(entity.name());
  }

  public Scene bind(Entity... entities) {
    for (Entity entity : entities)
      packets.bind(entity.parameters().shader(), entity.name());
    return this;
  }

  public Scene unbind(Entity... entities) {
    for (Entity entity : entities)
      packets.unbind(entity.parameters().shader(), entity.name());
    return this;
  }

  public boolean hasPacket(ERenderer shader) {
    return packets.contains(shader);
  }

  public void rayCastMouseClick(boolean all, IHitListener listener) {
    Raycaster.rayCastMouseClick(listener,
                                all,
                                entities.getAll(),
                                window,
                                camera,
                                projectionMat(EProjection.PERSPECTIVE)
    );
  }

  public Matrix4f modelViewMat3D(Entity entity) {
    return modelWorldSpaceMat(entity, camera.view3D());
  }

  public Matrix4f modelViewMat2D(Entity entity) {
    return modelWorldSpaceMat(entity, camera.view2D());
  }

  public Matrix4f modelProjectionMat(Entity entity) {
    return modelWorldSpaceMat(entity, projectionMat(entity.projection()));
  }

  Matrix4f modelWorldSpaceMat(Entity entity, Matrix4f mat) {
    return new Matrix4f().set(mat).mul(entity.transform().worldModelMat());
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
    diagnostics.capture(this);
    GlobalCache.instance().dispose();
    audio.dispose();
    window.dispose();
  }
}
