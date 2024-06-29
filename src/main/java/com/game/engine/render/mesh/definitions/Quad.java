package com.game.engine.render.mesh.definitions;

import com.game.engine.render.mesh.MeshInfo;
import com.game.utils.enums.EAttribute;

public class Quad extends MeshInfo {
  public static final float[] POSITIONS = {
    -0.5f, 0.5f, 0.5f,
    -0.5f, -0.5f, 0.5f,
    0.5f, -0.5f, 0.5f,
    0.5f, 0.5f, 0.5f,
    };
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
  public static final String ID = "quad";

  public Quad() {
    super(ID);
    addVertices(POSITIONS, 3, EAttribute.POS.getValue()).addVertices(TEXTURE_COORDINATES, 2, EAttribute.TXC.getValue())
                                                        .addVertices(COLORS, 3, EAttribute.CLR.getValue())
                                                        .setIndices(INDICES);
  }
}
