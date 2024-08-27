package com.game.caches.shaders;

import com.game.engine.render.mesh.vertices.AttribInfo;
import com.game.engine.render.mesh.vertices.VertexAttributeArray;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.application.LambdaCounter;
import org.lwjgl.opengl.GL46;

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

  public List<String> getAttributeList() {
    return listOf(
      "Attributes",
      GL46.GL_ACTIVE_ATTRIBUTES,
      (i, s, t) -> GL46.glGetActiveAttrib(programId, i, s, t)
    );
  }

  public VertexAttributeArray point(VertexInfo info) {
    Collection<AttribInfo> attributes = info.attributes().values();
    VertexAttributeArray vertexAttributeArray = new VertexAttributeArray(
      info.glType(),
      info.totalSize()
    );
    LambdaCounter lambdaOffset = new LambdaCounter();
    attributes.stream().sorted((a, b) -> get(a.key()) - get(b.key())).forEach(attribute -> {
      String attributeName = attribute.key();
      int location = get(attributeName);
      if (location < 0) return;

      int size = attribute.size();

      for (int i = 0; i < attribute.dimensions(); i++) {
        int offset = lambdaOffset.value();
        vertexAttributeArray.addVertexAttribute(
          attribute.key(),
          location,
          size,
          offset,
          attribute.divisor()
        );

        lambdaOffset.add(size);
        location++;
      }
    });

    return vertexAttributeArray;
  }
}
