package com.game.engine.render.mesh;

import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.application.ValueStore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class MeshInfo {
  protected final List<VertexInfo> vertices;
  protected final ValueStore indices;
  protected final String name;
  protected int vertexCount;
  protected int instances;

  public MeshInfo(String name) {
    this.name = name;

    indices = new ValueStore();
    vertices = new ArrayList<>();
    instances = 1;
    vertexCount = 0;
  }

  public boolean isInstanced() {
    return instances > 1;
  }

  public boolean isComplex() {
    return indices.size() > 0;
  }

  public MeshInfo setIndices(int... indices) {
    ValueStore store = new ValueStore();
    store.add(indices);
    return setIndices(store);
  }

  public MeshInfo setIndices(ValueStore indices) {
    this.indices.set(indices);
    vertexCount = indices.size();
    return this;
  }

  public MeshInfo addVertices(VertexInfo vertexInfo) {
    if (vertexInfo == null) return this;
    this.vertices.add(vertexInfo);
    if (vertexCount == 0) vertexCount = vertexInfo.numVertices();
    return this;
  }

  public MeshInfo addVertices(float[] values, int size, String attribute) {
    ValueStore store = new ValueStore();
    store.add(values);

    return addVertices(store, size, attribute);
  }

  public MeshInfo addVertices(ValueStore store, int size, String attribute) {
    return addVertices(store, GL46.GL_FLOAT, size, attribute);
  }

  public MeshInfo addVertices(ValueStore values, int glType, int size, String attribute) {
    return addVertices(values, glType, size, attribute, 1);
  }

  public MeshInfo addVertices(ValueStore values, int glType, int size, String attribute, int instances) {
    if (instances > this.instances) this.instances = instances;

    if (values.size() > 0 && size > 0) {
      AttribInfo attribInfo = new AttribInfo(attribute, size, instances);
      VertexInfo vertexInfo = new VertexInfo(values, glType, attribInfo);

      addVertices(vertexInfo);
    }

    return this;
  }
}
