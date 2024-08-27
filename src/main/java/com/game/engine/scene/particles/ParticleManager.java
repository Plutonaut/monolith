package com.game.engine.scene.particles;

import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.models.Model;
import com.game.graphics.materials.Material;
import com.game.utils.application.RandomNumberGenerator;
import com.game.utils.application.values.ValueMap;
import com.game.utils.enums.EMaterialTexture;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;

import java.util.HashMap;

@Slf4j
public class ParticleManager {
  protected final HashMap<String, ParticleSpawner> spawners;
  protected final Vector3f velocity;
  protected final RandomNumberGenerator rng;
  protected int maxParticles;
  protected float[] particleLifetimes;

  public ParticleManager(int maxParticles, Vector3f velocity, int randomSeed, float[] particleLifetimes) {
    this.velocity = velocity;
    this.rng = new RandomNumberGenerator(randomSeed);
    this.maxParticles = maxParticles;
    this.particleLifetimes = particleLifetimes;

    spawners = new HashMap<>();
  }

  public Model generate(ValueMap map) {
    String id = map.get("id");
    String[] names = map.getArr("spawners");
    Vector3f position = map.getVector3f("position");
    Material material = new Material(id + "_mat");
    if (map.has("diffuseTexturePath")) material.texture(
      EMaterialTexture.DIF.value(),
      map.get("diffuseTexturePath")
    );
    Model model = new Model(id);
    for (String name : names) {
      ParticleSpawner spawner = spawner(name, position);
      MeshInfo info = spawner.createMeshInfo();
      info.material(material);
      model.addMeshData(info.name());
    }

    return model;
  }

  public void onPreRender(float delta) {
    spawners.values().forEach(spawner -> spawner.preRender(delta));
  }

  public void onPostRender() {
    spawners.values().forEach(ParticleSpawner::postRender);
  }

  public ParticleSpawner spawner(String name) {
    return spawners.getOrDefault(name, null);
  }

  public ParticleSpawner spawner(String name, Vector3f position) {
    return spawners.computeIfAbsent(
      name,
      (key) -> new ParticleSpawner(
        key,
        position,
        velocity,
        maxParticles,
        particleLifetimes,
        rng.texture1D(maxParticles)
      )
    );
  }
}
