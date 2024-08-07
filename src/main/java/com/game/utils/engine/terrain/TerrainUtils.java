package com.game.utils.engine.terrain;

import com.game.utils.application.values.ValueStore;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

public class TerrainUtils {
  public static final float X_AXIS = -0.5f;
  public static final float Z_AXIS = -0.5f;
  public static final int MAX_COLOR = 255 * 255 * 255;
  public static final float TEX_COORD_INC = 40f; // Offset used to tile texture across terrain.

  public static float incrementX(int side) {
    return increment(X_AXIS, side);
  }

  public static float incrementZ(int side) {
    return increment(Z_AXIS, side);
  }

  static float increment(float axis, int side) {
    return Math.abs(-axis * 2) / (side - 1);
  }

  // frog - byte instantiation logic replaced with for loop.
  public static float getHeight(
    int x, int z, float minY, float maxY, int width, ByteBuffer buffer
  ) {
    byte[] byteColorArray = new byte[4];
    for (int i = 0; i < byteColorArray.length; i++)
      byteColorArray[i] = buffer.get(x * 4 + i + z * 4 * width);
    final int argb = (0xFF & byteColorArray[3]) << 24 | (0xFF & byteColorArray[0]) << 16 | (0xFF & byteColorArray[1]) << 8 | 0xFF & byteColorArray[2];
    return minY + Math.abs(maxY - minY) * ((float) argb / (float) MAX_COLOR);
  }

  public static ValueStore calculateNormals(ValueStore positions, int width, int height) {
    return calculateNormals(positions.asArray(), width, height);
  }

  public static ValueStore calculateNormals(float[] positionArray, int width, int height) {
    final Vector3f v0 = new Vector3f();
    Vector3f v1 = new Vector3f();
    Vector3f v2 = new Vector3f();
    Vector3f v3 = new Vector3f();
    Vector3f v4 = new Vector3f();
    final Vector3f v12 = new Vector3f();
    final Vector3f v23 = new Vector3f();
    final Vector3f v34 = new Vector3f();
    final Vector3f v41 = new Vector3f();
    final ValueStore normals = new ValueStore();
    Vector3f normal = new Vector3f();
    for (int row = 0; row < height; row++)
      for (int col = 0; col < width; col++) {
        if (row > 0 && row < height - 1 && col > 0 && col < width - 1) {
          final int i0 = row * width * 3 + col * 3;
          v0.x = positionArray[i0];
          v0.y = positionArray[i0 + 1];
          v0.z = positionArray[i0 + 2];

          final int i1 = row * width * 3 + (col - 1) * 3;
          v1.x = positionArray[i1];
          v1.y = positionArray[i1 + 1];
          v1.z = positionArray[i1 + 2];
          v1 = v1.sub(v0);

          final int i2 = (row + 1) * width * 3 + col * 3;
          v2.x = positionArray[i2];
          v2.y = positionArray[i2 + 1];
          v2.z = positionArray[i2 + 2];
          v2 = v2.sub(v0);

          final int i3 = row * width * 3 + (col + 1) * 3;
          v3.x = positionArray[i3];
          v3.y = positionArray[i3 + 1];
          v3.z = positionArray[i3 + 2];
          v3 = v3.sub(v0);

          final int i4 = (row - 1) * width * 3 + col * 3;
          v4.x = positionArray[i4];
          v4.y = positionArray[i4 + 1];
          v4.z = positionArray[i4 + 2];
          v4 = v4.sub(v0);

          v1.cross(v2, v12);
          v12.normalize();

          v2.cross(v3, v23);
          v23.normalize();

          v3.cross(v4, v34);
          v34.normalize();

          v4.cross(v1, v41);
          v41.normalize();

          normal = v12.add(v23).add(v34).add(v41);
          normal.normalize();
        } else {
          normal.x = 0;
          normal.y = 1;
          normal.z = 0;
        }
        normal.normalize();
        normals.add(normal.x);
        normals.add(normal.y);
        normals.add(normal.z);
      }
    return normals;
  }
}
