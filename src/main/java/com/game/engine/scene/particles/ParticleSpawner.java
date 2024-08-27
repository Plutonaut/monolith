package com.game.engine.scene.particles;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.graphics.texture.Texture;
import com.game.utils.application.values.ValueStore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

@Accessors(fluent = true)
@Data
public class ParticleSpawner {
  String name;
  Particle[] particles;
  Texture texture;
  float time;
  float deltaTime;
  float launcherLifetime;
  float shellLifetime;
  float fragLifetime;
  int currentVBO;
  int currentTFB;
  boolean initialDraw;

  public ParticleSpawner(
    String name, Vector3f position, Vector3f velocity, int maxParticles, float[] particleLifetimes, Texture texture
  ) {
    this.texture = texture;
    this.name = name;
    this.particles = new Particle[maxParticles];
    this.launcherLifetime = particleLifetimes[0];
    this.shellLifetime = particleLifetimes[1];
    this.fragLifetime = particleLifetimes[2];

    particles[0] = new Particle();
    particles[0].position.set(position);
    particles[0].velocity.set(velocity);
    for (int i = 1; i < maxParticles; i++)
      particles[i] = new Particle();
    time = 0.0f;
    deltaTime = 0.0f;
    currentVBO = 0;
    currentTFB = 1;
    initialDraw = false;
  }

  public void preRender(float delta) {
    time += delta;
    deltaTime = delta;
  }

  public void bindTexture(int index) {
    texture.active(index);
    texture.bind();
  }

  public void postRender() {
    currentVBO = currentTFB;
    currentTFB = (currentTFB + 1) & 0x1;
  }

  public ValueStore serialized() {
    ValueStore store = new ValueStore();
    for (Particle particle : particles) store.add(particle.serialized());
    return store;
  }

  public MeshInfo createMeshInfo() {
    return GlobalCache.instance().meshInfo(name, (n) -> {
      MeshInfo info = new MeshInfo(name);
      info.drawMode(GL46.GL_POINTS);
      info.vertexCount(particles.length);
      ValueStore store = serialized();
      for (int i = 0; i < 2; i++) {
        VertexInfo vertex = Particle.createVertexAttributeArray(store);
        vertex.transformFeedback(i);
        vertex.customName("particle_%d".formatted(i));
        info.vertices().add(vertex);
      }
      return info;
    });
  }
}
