package com.game.engine.render.mesh;

import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.render.models.IModel;
import com.game.graphics.materials.Material;
import com.game.utils.application.ValueStore;
import com.game.utils.engine.MaterialUtils;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.enums.EAttribute;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class MeshInfo implements IModel {
  protected final List<VertexInfo> vertices;
  protected final ValueStore indices;
  protected final String name;
  protected Material material;
  protected int vertexCount;
  protected int instances;

  public MeshInfo(String name) {
    this.name = name;

    indices = new ValueStore();
    vertices = new ArrayList<>();
    material = new Material(MaterialUtils.DFLT);
    instances = 1;
    vertexCount = 0;
  }

  @Override
  public ECache type() { return ECache.MESH_INFO; }

  public Mesh create() {
    Mesh mesh = isInstanced() ? new InstancedMesh(name, instances) : new Mesh(name);
    mesh.vertexCount(vertexCount);
    mesh.isComplex = !indices.isEmpty();
    mesh.material(material);
    VertexInfo positions = getVerticesByAttribute(EAttribute.POS.getValue());
    if (positions != null) {
      Bounds3D bounds = MeshInfoUtils.calculateBounds(positions);
      mesh.updateBounds(bounds.min(), bounds.max());
    }
    return mesh;
  }

  public boolean isInstanced() {
    return instances > 1;
  }

  public VertexInfo getVerticesByAttribute(String key) {
    return vertices
      .stream()
      .filter(vertexInfo -> vertexInfo.hasAttribute(key))
      .findFirst()
      .orElse(null);
  }

  public VertexInfo getVerticesByAttribute(EAttribute key) {
    return getVerticesByAttribute(key.getValue());
  }

  public MeshInfo addVertices(VertexInfo vertexInfo) {
    if (vertexInfo != null && vertices.size() < MeshInfoUtils.MAX_VERTEX_DATA)
      this.vertices.add(vertexInfo);
    return this;
  }

  public MeshInfo addVertices(
    ValueStore values, int glType, int glUsage, int size, String attribute, int instances
  ) {
    if (instances > this.instances) this.instances = instances;

    if (!values.isEmpty() && size > 0) {
      AttribInfo attribInfo = new AttribInfo(attribute, size, instances);
      VertexInfo vertexInfo = new VertexInfo(values, glType, glUsage, attribInfo);

      addVertices(vertexInfo);
    }

    return this;
  }
}
