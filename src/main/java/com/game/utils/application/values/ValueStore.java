package com.game.utils.application.values;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIVector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ValueStore {
  protected final List<Float> values;

  public ValueStore() {
    this.values = new ArrayList<>();
  }

  public int size() {
    return values.size();
  }

  public boolean isEmpty() { return values.isEmpty(); }

  public Stream<Float> asStreamf() {
    return values.stream();
  }

  public IntStream asStreami() {
    return asStreamf().mapToInt(Math::round);
  }

  public float[] asArray() {
    float[] values = new float[size()];
    for (int i = 0; i < values.length; i++) values[i] = this.values.get(i);
    return values;
  }

  public int[] asIntArray() {
    int[] values = new int[size()];
    for (int i = 0; i < values.length; i++) values[i] = Math.round(this.values.get(i));
    return values;
  }

  public void set(float[] values) {
    clear();
    add(values);
  }

  public void set(int[] values) {
    clear();
    add(values);
  }

  public void set(ValueStore store) {
    clear();
    add(store);
  }

  public void add(Stream<Float> values) {
    values.forEach(this::add);
  }

  public void add(ValueStore values) {
    add(values.asStreamf());
  }

  public void add(float... values) {
    if (values == null) return;
    for (float v : values) this.values.add(v);
  }

  public void add(int[] values) {
    if (values == null) return;
    for (float value : values) add(value);
  }

  public void add(Matrix4f value) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++)
        add(value.get(i, j));
    }
  }

  public void add(Vector4f value) {
    add(value.x, value.y, value.z, value.w);
  }

  public void add(Vector3f value) {
    add(value.x, value.y, value.z);
  }

  public void add(Vector2f value) {
    add(value.x, value.y);
  }

  public void add(AIVector3D value, boolean addZ) {
    add(value.x());
    add(value.y());
//    add(invertY ? 1 - value.y() : value.y());

    if (addZ) add(value.z());
  }

  public void clear() {
    values.clear();
  }
}
