package com.game.caches.shaders;

import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexAttributeArray;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.application.LambdaCounter;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AttributeCache extends AbstractShaderCache {
  public AttributeCache(int programId) {
    super(programId);
  }

  @Override
  protected int glLocation(String key) {
    int location = GL46.glGetAttribLocation(programId, key);
    return check(location, "Attribute", key);
  }

  public List<VertexAttributeArray> point(VertexInfo info) {
    Collection<AttribInfo> attributes = info.attributes().values();
    int glType = info.glType();

    List<VertexAttributeArray> vertexAttributeArrays = new ArrayList<>();
    LambdaCounter lambdaOffset = new LambdaCounter();

    int stride = attributes.size() == 1 ? 0 : attributes.stream().mapToInt(AttribInfo::size).sum();
    int offset = lambdaOffset.value();

    attributes.forEach(attribute -> {
      VertexAttributeArray vertexAttributeArray = new VertexAttributeArray(
        attribute.key(),
        attribute.size(),
        stride,
        offset,
        glType,
        attribute.instances()
      );
      point(vertexAttributeArray);
      vertexAttributeArrays.add(vertexAttributeArray);
    });

    return vertexAttributeArrays;
  }

  public void point(VertexAttributeArray vertexAttributeArray) {
    int location = get(vertexAttributeArray.key());
    if (location < 0) return;
    boolean instanced = vertexAttributeArray.isInstanced();
    for (int i = 0; i < vertexAttributeArray.instances(); i++) {
      location += i;
      GL46.glVertexAttribPointer(
        location,
        vertexAttributeArray.size(),
        vertexAttributeArray.glType(),
        false,
        vertexAttributeArray.glStride(),
        vertexAttributeArray.glOffset(i)
      );
      if (instanced) GL46.glVertexBindingDivisor(location, 1);
    }
  }

  interface IVertexAttributeOperation {
    void gl(int location);
  }

  public void enable(VertexAttributeArray vertexAttributeArray) {
    operate(vertexAttributeArray, GL46::glEnableVertexAttribArray);
  }

  public void disable(VertexAttributeArray vertexAttributeArray) {
    operate(vertexAttributeArray, GL46::glDisableVertexAttribArray);
  }

  void operate(VertexAttributeArray vertexAttributeArray, IVertexAttributeOperation operation) {
    String key = vertexAttributeArray.key();
    int location = get(key);
    if (location < 0) return;
    int instances = vertexAttributeArray.instances();
    for (int i = 0; i < instances; i++) {
      location += i;
      operation.gl(location);
    }
  }
}
