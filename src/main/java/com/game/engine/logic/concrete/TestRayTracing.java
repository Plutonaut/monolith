package com.game.engine.logic.concrete;

import com.game.caches.GlobalCache;
import com.game.engine.compute.Instruction;
import com.game.engine.logic.AbstractLogic;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.Entity;
import com.game.engine.settings.EngineSettings;
import com.game.utils.application.RandomNumberGenerator;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.EGLParam;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import org.joml.Vector3f;

public class TestRayTracing extends AbstractLogic {
  static final int size = 1024;
  static final int sphere_size = 8;
  static final int sphere_count = 32;

  Sphere[] spheres = new Sphere[sphere_count];
  RandomNumberGenerator rng;
  Instruction in;
  Entity screenQuad;

  public TestRayTracing(EngineSettings settings) {
    super(settings);
  }

  Sphere generate() {
    Vector3f center = new Vector3f(
      rng.nextf(3.0f, 10.0f),
      rng.nextf(-8.0f, 8.0f),
      rng.nextf(-8.0f, 8.0f)
    );
    float radius = rng.nextf(0.1f, 2.0f);
    Vector3f color = rng.nextv3(0.3f, 1.0f);

    return new Sphere(center, radius, color, 0f);
  }

  void updateScene() {
    for (int i = 0; i < sphere_count; i++) {
      Sphere sphere = spheres[i];
      float[] data = sphere.serialized().asArray();
      in.record(data);
    }
  }

  @Override
  protected String windowTitle() {
    return "Raytracing test!";
  }

  @Override
  public void onStart() {
    in = new Instruction("compute_test", 1, scene.window().width(), scene.window().height());
    // Increasing or decreasing initial buffer size yields no improvement on performance.
    in.init(size * sphere_size);
    in.uniforms().setInt("sphereCount", sphere_count);
    rng = new RandomNumberGenerator(0);

    for (int i = 0; i < sphere_count; i++) spheres[i] = generate();
    float[] texcoords = new float[] {
      1, 1,   // top-right
      -1, 1,  // top-left
      -1, -1, // bottom-left
      -1, -1, // bottom-left
      1, -1,  // bottom-right
      1, 1    // top-right
    };
    MeshInfo info = GlobalCache.instance().meshInfo(
      "screen_quad",
      (name) -> new MeshInfoBuilder()
        .use("screen_quad")
        .textureCoordinates(texcoords)
        .material(
          "screen_quad_mat")
        .materialTexture(
          EMaterialTexture.OUT.value(),
          in.frame().key()
        )
        .build()
    );
    info.material().texture(EMaterialTexture.OUT.value(), in.frame().key());
    Model m = new Model("screen_quad_model");
    m.addMeshData(info.name());
    screenQuad = scene.createEntity(
      "screen_quad",
      m,
      ERenderer.FRAMEBUFFER,
      EProjection.ORTHOGRAPHIC_CENTER_2D,
      EGLParam.BLEND
    );
    updateScene();
    screenQuad.instructions().add(in);
    scene.bind(screenQuad).bind(ERenderer.RAYTRACE, screenQuad.name());
  }

  @Override
  public void input() {
    captureCameraMovementInput();
  }

  @Override
  public void update(float interval) {
    moveCameraOnUpdate();
  }

  record Sphere(Vector3f center, float radius, Vector3f color, float offset) {
    public ValueStore serialized() {
      ValueStore store = new ValueStore();
      store.add(center);
      store.add(radius);
      store.add(color);
      store.add(offset);
      return store;
    }
  }
}
