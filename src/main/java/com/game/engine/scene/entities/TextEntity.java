package com.game.engine.scene.entities;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.EAttribute;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Accessors(fluent = true)
@Data
public class TextEntity implements IRenderable {
  private final ModelTransform transform;
  private final Mesh mesh;
  private final String name;
  private String text;
  private int positionVboId;
  private int textureCoordinateVboId;
  private int indexVboId;

  public TextEntity(String name, Mesh mesh, String text) {
    this.transform = new ModelTransform();
    this.name = name;
    this.mesh = mesh;
    this.text = text;
  }

  public TextEntity move(float x, float y) {
    transform.position().set(x, y, 0f);
    return this;
  }

  public TextEntity scale(float s) {
    transform.scale(s);
    return this;
  }

  public void update(FontMeshInfo info) {
    mesh.bind();
    info.vertices().forEach(this::updateVbo);
    if (mesh.isComplex()) updateIndexVbo(info.indices().asIntArray());
    mesh.unbind();
  }

  public void updateVbo(VertexInfo info) {
    int id = info.hasAttribute(EAttribute.POS.getValue()) ? positionVboId : textureCoordinateVboId;
    VertexBufferObject vbo = mesh.vbo(id);
    float[] values = info.vertices().asArray();
    updateVbo(values, vbo);
  }

  void updateVbo(float[] values, VertexBufferObject vbo) {
    int size = values.length;
    vbo.bind();
    vbo.upload(size, GL46.GL_DYNAMIC_DRAW);
    FloatBuffer buffer = MemoryUtil.memAllocFloat(size).put(values).flip();
    vbo.subUpload(buffer, 0);
  }

  public void updateIndexVbo(int[] indices) {
    IndexBufferObject ibo = (IndexBufferObject) mesh.vbo(indexVboId);
    int size = indices.length;
    ibo.bind();
    ibo.upload(size, GL46.GL_DYNAMIC_DRAW);
    IntBuffer buffer = MemoryUtil.memAllocInt(size).put(indices).flip();
    ibo.subUpload(buffer, 0);
  }
}
