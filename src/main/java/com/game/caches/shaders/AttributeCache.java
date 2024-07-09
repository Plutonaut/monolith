package com.game.caches.shaders;

import com.game.engine.render.mesh.vertices.AttribInfo;
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

  public List<String> point(Collection<AttribInfo> attributes, int glType) {
    List<String> vaas = new ArrayList<>();
    LambdaCounter lambdaOffset = new LambdaCounter();
    int stride = attributes.size() == 1 ? 0 : attributes.stream().mapToInt(AttribInfo::size).sum();
    int offset = lambdaOffset.value();
    attributes.forEach(attribute -> {
      if (has(attribute.key())) {
        enable(attribute.key());
        point(
          attribute.key(),
          attribute.size(),
          stride,
          offset,
          glType,
          attribute.instances()
        );
        lambdaOffset.add(attribute.size() * attribute.instances());
        vaas.add(attribute.key());
      }
    });

    return vaas;
  }

  public void point(String attribute, int size, int stride, int offset, int glType, int instances) {
    int location = get(attribute);
    if (location < 0) return;

    boolean instanced = instances > 1;
    int glBytes = glBytes(glType);
    int glStride = stride * glBytes * instances;
    for (int i = 0; i < instances; i++) {
      location += i;
      GL46.glVertexAttribPointer(location, size, glType, false, glStride, (long) offset * glBytes);
      if (instanced) GL46.glVertexBindingDivisor(location, 1);
      offset += size;
    }
  }

  public int glBytes(int glType) {
    return glType == GL46.GL_FLOAT ? Float.BYTES : Integer.BYTES;
  }

  public void enable(String key) {
    int location = get(key);
    if (location >= 0) GL46.glEnableVertexAttribArray(location);
  }

  public void disable(String key) {
    int location = get(key);
    if (location >= 0) GL46.glDisableVertexAttribArray(location);
  }
}
