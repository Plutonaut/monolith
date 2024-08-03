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
import com.game.engine.scene.generators.IGeneratorConsumer;
import com.game.engine.scene.generators.ModelGenerator;
import com.game.engine.scene.lighting.LightingManager;
import com.game.engine.scene.projection.Projection;
import com.game.engine.settings.EngineSettings;
import com.game.engine.window.Window;
import com.game.graphics.materials.Material;
import com.game.graphics.texture.TextureMapData;
import com.game.utils.engine.TextureUtils;
import com.game.utils.engine.entity.ParameterUtils;
import com.game.utils.engine.logging.DiagnosticLoggingHandler;
import com.game.utils.enums.EGLParam;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.stream.Stream;

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
  private final DiagnosticLoggingHandler diagnostics;

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
    parameters = new GLParameters();
    entities = new EntityManager();
    models = new ModelGenerator();
    diagnostics = new DiagnosticLoggingHandler();
  }

  public AudioSource audio(String key, String path) {
    return audio.load(key, path);
  }

  public Entity entity(String id) {
    return entities.get(id);
  }

  public Entity create(String id, String modelId, ERenderer shader) {
    return create(id, modelId, shader, EProjection.PERSPECTIVE);
  }

  public Entity create(String id, String modelId, ERenderer shader, EProjection projection) {
    return create(id, modelId, shader, projection, null);
  }

  public Entity create(
    String id, String modelId, ERenderer shader, EProjection projection, IGeneratorConsumer consumer
  ) {
    return create(id, modelId, shader, projection, consumer, 3);
  }

  public Entity createText(String id, String text) {
    return create(
      id,
      id + "_" + text,
      ERenderer.FONT,
      EProjection.ORTHOGRAPHIC_FONT_2D,
      generator -> generator.generateText(id, text),
      EGLParam.BLEND,
      EGLParam.DEPTH
    );
  }

  public Entity createModel(String id, String path) {
    return create(
      id,
      path,
      ERenderer.SCENE,
      EProjection.PERSPECTIVE,
      generator -> generator.generate3DModel(id, path),
      EGLParam.DEPTH,
      EGLParam.BLEND,
      EGLParam.CULL
    );
  }

  // TODO: Remove dependency on texture map
  // TODO: Separate procedural terrain generation out into it's own more complex map generator.
  public Entity generateProceduralTerrain(String id, int size) {
    return generateProceduralTerrain(id, size, TextureUtils.MOSS_TEXTURE_MAP_DATA);
  }

  public Entity generateProceduralTerrain(
    String id, int size, String diffuseTexture, String heightTexture
  ) {
    TextureMapData textureMapData = new TextureMapData()
      .diffuse(diffuseTexture)
      .height(heightTexture);
    return generateProceduralTerrain(id, size, textureMapData);
  }

  Entity generateProceduralTerrain(String id, int size, TextureMapData textureMapData) {
    return create(
      id,
      "proc_terrain" + id,
      ERenderer.SCENE,
      EProjection.PERSPECTIVE,
      generator -> generator.generateProceduralTerrain(id, textureMapData, size, size),
      EGLParam.CULL,
      EGLParam.DEPTH,
      EGLParam.BLEND
    );
  }

  public Entity createSkyBox(String id, String path) {
    return create(
      id,
      path,
      ERenderer.SKYBOX,
      EProjection.PERSPECTIVE,
      generator -> generator.generate3DModel(id, path),
      EGLParam.DEPTH,
      EGLParam.BLEND
    );
  }

  public Entity createSprite(String id, String path) {
    return create(
      id,
      path,
      ERenderer.SPRITE,
      EProjection.ORTHOGRAPHIC_FONT_2D,
      generator -> generator.generate2DModel(id, path),
      EGLParam.BLEND,
      EGLParam.DEPTH
    );
  }

  public Entity create(
    String id,
    String modelId,
    ERenderer shader,
    EProjection projection,
    IGeneratorConsumer consumer,
    EGLParam... params
  ) {
    int glFlags = ParameterUtils.toIntFlagValue(params);
    return create(id, modelId, shader, projection, consumer, glFlags);
  }

  public Entity create(
    String id,
    String modelId,
    ERenderer shader,
    EProjection projection,
    IGeneratorConsumer consumer,
    int glFlags
  ) {
    Model model = GlobalCache.instance().model(modelId, model_id -> consumer.consume(models));
    return create(id, model, shader, projection, glFlags);
  }

  public Entity create(
    String id, Model model, ERenderer shader, EProjection projection, EGLParam... params
  ) {
    int glFlags = Stream.of(params).mapToInt(EGLParam::value).reduce((u, v) -> u | v).orElse(0);
    return create(id, model, shader, projection, glFlags);
  }

  public Entity create(
    String id, Model model, ERenderer shader, EProjection projection, int glFlags
  ) {
    return createEntity(id, model, shader, projection, glFlags, false);
  }

  public Entity copy(
    String id, String modelId, ERenderer shader, EProjection projection, int glFlags
  ) {
    Model model = GlobalCache.instance().model(modelId);
    return copy(id, model, shader, projection, glFlags);
  }

  public Entity copy(
    String id, Model model, ERenderer shader, EProjection projection, int glFlags
  ) {
    return createEntity(id, model, shader, projection, glFlags, true);
  }

  Entity createEntity(
    String id, Model model, ERenderer shader, EProjection projection, int glFlags, boolean useCache
  ) {
    EntityRenderParameters parameters = new EntityRenderParameters(shader, projection, glFlags);
    return entities.create(id, model, parameters, useCache);
  }

  public Entity copy(Entity from) {
    return copy(from.name());
  }

  public Entity copy(String entityName) {
    return entities.createFrom(entityName);
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
    Raycaster.rayCastMouseClick(
      listener,
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

  // TODO: Display text that says gathering diagnostics and then pause rendering.
  public void captureDiagnostics() {
    diagnostics.init("Scene Diagnostics");
    diagnostics
      .open(window.title())
      .row("width", window.width())
      .row("height", window().height())
      .close();
    diagnostics
      .open("Camera")
      .row("Position", camera.position())
      .row("Rotation", camera.rotation())
      .row("Mouse Sensitivity: ", camera.mouseSensitivity())
      .row("Camera Speed: ", camera.cameraSpeed())
      .close();
    diagnostics.open("Lighting");
    lighting.lighting().forEach((key, value) -> {
      diagnostics.row(key);
      diagnostics.row("Color", value.color());
      diagnostics.row("Factor", value.factor());
    });
    diagnostics.close();

    diagnostics.open("Entities");
    entities.entities().forEach((key, value) -> {
      diagnostics.row(key, value.id())
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
        mesh.vaas().forEach((a, v) ->
                              diagnostics
                                .row("Vertex Attribute Array", a)
                                .row("Size", v.size())
                                .row("Stride", v.stride())
                                .row("Offset", v.offset())
                                .row("Instances", v.instances())

        );
      });
    });
    GlobalCache.instance().computeDiagnostics(diagnostics);
    diagnostics.close();
    diagnostics.dispose();
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
