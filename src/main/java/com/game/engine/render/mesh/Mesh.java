package com.game.engine.render.mesh;

import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexAttributeArray;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.graphics.IGraphics;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.utils.application.values.ValueStore;
import com.game.utils.enums.EAttribute;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@Accessors(fluent = true)
@Data
public class Mesh implements IGraphics {
  protected final int glId;
  protected final String name;
  protected final HashSet<VertexAttributeArray> vaas;
  protected final HashMap<String, VertexBufferObject> vboAttributeKeyMap;
  protected final Bounds3D bounds;
  protected Material material;
  protected int drawMode;
  protected int vertexCount;
  protected boolean isComplex;
  boolean initialized = false;
  boolean selected = false;

  public Mesh(String name) {
    this.name = name;

    glId = GL46.glGenVertexArrays();
    vboAttributeKeyMap = new HashMap<>();
    vaas = new HashSet<>();
    bounds = new Bounds3D();
    drawMode = GL46.GL_TRIANGLES;
  }

  @Override
  public ECache type() { return ECache.MESH; }

  @Override
  public String key() {
    return name;
  }

  @Override
  public void bind() {
    GL46.glBindVertexArray(glId);
  }

  @Override
  public void dispose() {
    unbind();
    GL46.glDeleteVertexArrays(glId);
    vboAttributeKeyMap.values().forEach(VertexBufferObject::dispose);
  }

  public void render() {
    bind();
    enable();
    draw(drawMode);
    disable();
    unbind();
  }

  protected void draw(int mode) {
    if (isComplex) drawComplex(mode);
    else drawSimple(mode);
  }

  protected void drawComplex(int mode) {
    drawComplex(mode, vertexCount);
  }

  protected void drawComplex(int mode, int vertexCount) {
    GL46.glDrawElements(mode, vertexCount, GL46.GL_UNSIGNED_INT, 0);
  }

  protected void drawSimple(int mode) {
    GL46.glDrawArrays(mode, 0, vertexCount);
  }

  public void setVertexAttributeArray(VertexAttributeArray vertexAttributeArray) {
    vaas.add(vertexAttributeArray);
  }

  public void enable() {
    vaas.forEach(VertexAttributeArray::enable);
  }

  public void disable() {
    vaas.forEach(VertexAttributeArray::disable);
  }

  public void updateBounds(Vector3f min, Vector3f max) {
    this.bounds.min().set(min);
    this.bounds.max().set(max);
  }

  public void redrawAttributes(MeshInfo info, Program program) {
    redraw(info, (v) -> setVertexAttributeArray(program.attributes().point(v)));
  }

  public void redraw(MeshInfo info, IVertexCallback callback) {
    bind();
    try (MemoryStack stack = MemoryStack.stackPush()) {
      info.vertices().forEach(vertex -> {
        VertexBufferObject vbo = vboAttributeKeyMap.computeIfAbsent(
          vertex.getAttributeKey(),
          (key) -> new VertexBufferObject()
        );
        updateVboData(vbo, vertex.vertices().asArray(), stack);
        if (callback != null) callback.onComplete(vertex);
      });
      if (isComplex()) {
        IndexBufferObject vbo = (IndexBufferObject) vboAttributeKeyMap.computeIfAbsent(
          "indices",
          (key) -> new IndexBufferObject()
        );
        updateIndexVboData(vbo, info.indices().asIntArray(), stack);
      }
    }
    unbind();
    initialized = true;
  }

  public void redraw(EAttribute attribute, ValueStore values) {
    bind();
    try (MemoryStack stack = MemoryStack.stackPush()) {
      VertexBufferObject vbo = vboAttributeKeyMap.get(attribute.getValue());
      updateVboData(vbo, values.asArray(), stack);
    }
    unbind();
  }

  void updateIndexVboData(
    IndexBufferObject indexVbo, int[] values, MemoryStack stack
  ) {
    int length = values.length * Integer.BYTES;
    boolean useManaged = length >= stack.getPointer();
    IntBuffer buffer = useManaged
                       ? MemoryUtil.memAllocInt(values.length).put(values).flip()
                       : stack.ints(values);
    int size = values.length;
    indexVbo.bind();
    indexVbo.upload((long) values.length * Integer.BYTES, GL46.GL_DYNAMIC_DRAW);
    indexVbo.subUpload(buffer, 0);

    if (vertexCount != size) vertexCount = size;

    if (useManaged) MemoryUtil.memFree(buffer);
  }

  void updateVboData(VertexBufferObject vbo, float[] values, MemoryStack stack) {
    int length = values.length * Float.BYTES;
    boolean useManaged = length >= stack.getPointer();
    FloatBuffer buffer = useManaged
                         ? MemoryUtil.memAllocFloat(values.length).put(values).flip()
                         : stack.floats(values);
    vbo.bind();
    vbo.upload((long) values.length * Float.BYTES, GL46.GL_DYNAMIC_DRAW);
    vbo.subUpload(buffer, 0);

    if (useManaged) MemoryUtil.memFree(buffer);
  }

  public void unbind() {
    // unbind textures
    GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    // unbind buffers
    GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
    // unbind mesh
    GL46.glBindVertexArray(0);
  }
}
