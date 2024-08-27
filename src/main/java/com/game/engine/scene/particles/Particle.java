package com.game.engine.scene.particles;

import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.EParticle;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

@Accessors(fluent = true)
@Data
public class Particle {
  public static final int SIZE = 8;

  protected float type; // size - 1
  protected Vector3f position; // size - 3
  protected Vector3f velocity; // size - 3
  protected float age; // size - 1

  public Particle() {
    type = EParticle.SPAWNER.value();
    position = new Vector3f();
    velocity = new Vector3f();
    age = 0.0f;
  }

  public ValueStore serialized() {
    ValueStore store = new ValueStore();
    store.add(type);
    store.add(position);
    store.add(velocity);
    store.add(age);

    return store;
  }

  public static VertexInfo createVertexAttributeArray(ValueStore store) {
    return new VertexInfo(
      store,
      GL46.GL_FLOAT,
      GL46.GL_DYNAMIC_DRAW,
      new AttribInfo("type", 1, 1, 0),
      new AttribInfo("position", 3, 1, 0),
      new AttribInfo("velocity", 3, 1, 0),
      new AttribInfo("age", 1, 1, 0)
    );
  }
}
