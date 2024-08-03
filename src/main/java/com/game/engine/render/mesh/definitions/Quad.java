package com.game.engine.render.mesh.definitions;

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
    0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f,
    };
  public static final float[] COLORS = {
    0.5f, 0.0f, 0.0f,
    0.0f, 0.5f, 0.0f,
    0.0f, 0.0f, 0.5f,
    0.0f, 0.5f, 0.5f,
    };
  public static final int[] INDICES = {
    0, 1, 3, 3, 1, 2
  };

  @Override
  public String name() {
    return "QUAD";
  }

  public Quad() {
    super();
  }

  @Override
  protected void init() {
    positions(POSITIONS).textureCoordinates(TEXTURE_COORDINATES)
                        .colors(COLORS)
                        .indices(INDICES);
  }
}
