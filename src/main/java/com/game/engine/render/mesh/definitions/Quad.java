package com.game.engine.render.mesh.definitions;

import com.game.engine.physics.Bounds2D;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.utils.application.values.ValueStore;
import org.joml.Vector2f;
import org.joml.Vector3f;

// TODO: Make TexturedQuad object with texture path/coordinate overrides...
public class Quad extends MeshDefinition {
  public static final float[] POSITIONS = {
    -0.5f, 0.5f, 0f,
    -0.5f, -0.5f, 0f,
    0.5f, -0.5f, 0f,
    0.5f, 0.5f, 0f,
    };

  // frog - Making values smaller increases the size of the texture.
  // Making values larger has the inverse effect...
  public static final float[] TEXTURE_COORDINATES = {
    0f, 0f,
    0f, 1f,
    1f, 1f,
    1f, 0f,
    };
  public static final float[] COLORS = {
    0.5f, 0.0f, 0.0f,
    0.0f, 0.5f, 0.0f,
    0.0f, 0.0f, 0.5f,
    0.0f, 0.5f, 0.5f,
    };
  public static final int[] INDICES = {
    0, 1, 3,
    3, 1, 2
  };

  public Quad() {
    super();
  }

  public Quad(float width, float height) {
    this(new Vector2f(), width, height);
  }

  public Quad(Vector2f offset, float width, float height) {
    this(new Bounds2D(
      new Vector2f(),
      new Vector2f(-offset.x(), height + offset.y()),
      new Vector2f(width + offset.x(), -offset.y())
    ));
  }

  public Quad(Bounds2D bounds) {
    builder = new MeshInfoBuilder();

    Vector2f min = bounds.min();
    Vector2f max = bounds.max();
    Vector3f bl = new Vector3f(min.x(), min.y(), 0f);
    Vector3f tr = new Vector3f(max.x(), max.y(), 0f);
    Vector3f br = new Vector3f(max.x(), min.y(), 0f);
    Vector3f tl = new Vector3f(min.x(), max.y(), 0f);

    ValueStore positionStore = new ValueStore();

    positionStore.add(tl);
    positionStore.add(bl);
    positionStore.add(br);
    positionStore.add(tr);

    positions(positionStore.asArray())
      .textureCoordinates(TEXTURE_COORDINATES)
      .colors(COLORS)
      .indices(INDICES);
  }

  @Override
  public String name() {
    return "QUAD";
  }

  @Override
  protected void init() {
    positions(POSITIONS).textureCoordinates(TEXTURE_COORDINATES)
                        .colors(COLORS)
                        .indices(INDICES);
  }
}
