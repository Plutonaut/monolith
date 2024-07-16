package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.vertices.IndexBufferObject;
import com.game.engine.render.mesh.vertices.VertexBufferObject;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.TextPacketResult;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.TextEntity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EAttribute;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class FontRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() { return ERenderer.FONT; }

  @Override
  protected void render(IRenderable item, Scene scene) {
    TextEntity text = (TextEntity) item;
    Matrix4f projection = new Matrix4f()
      .set(scene.projectionMat(EProjection.ORTHOGRAPHIC_FONT_2D))
      .mul(text.transform().worldModelMat());
    program.uniforms().set(EUniform.PROJECTION.value(), projection);
    Mesh mesh = text.mesh();
    Material material = mesh.material();
    setMaterialUniform(material);
    draw(mesh);
  }

  @Override
  public TextPacketResult associate(Model model) {
    TextPacketResult result = new TextPacketResult();
    model.meshInfo().forEach(info -> {
      FontMeshInfo fontMeshInfo = (FontMeshInfo) info;
      result.text(fontMeshInfo.text());
//      Mesh mesh = associate(info);
      Mesh mesh = info.create();
      mesh.bind();
      info.vertices().forEach(vertex -> {
        VertexBufferObject vbo = new VertexBufferObject();
        int vboId = vbo.glId();
        if (vertex.hasAttribute(EAttribute.POS.getValue())) result.positionVboId(vboId);
        else if (vertex.hasAttribute(EAttribute.TXC.getValue()))
          result.textureCoordinateVboId(vboId);
        float[] values = vertex.vertices().asArray();
        int size = values.length;
        FloatBuffer buffer = MemoryUtil.memAllocFloat(size).put(values).flip();
        vbo.bind();
        vbo.upload((long) size * GL46.GL_FLOAT, GL46.GL_DYNAMIC_DRAW);
        vbo.subUpload(buffer, 0);
        mesh.vbos().add(vbo);
        List<String> vaas = program
          .attributes()
          .point(vertex.attributes().values(), vertex.glType());
        mesh.vaas().addAll(vaas);
        MemoryUtil.memFree(buffer);
      });
      if (mesh.isComplex()) {
        IndexBufferObject ibo = new IndexBufferObject();
        result.indexVboId(ibo.glId());
        int[] indices = info.indices().asIntArray();
        int size = indices.length;
        IntBuffer buffer = MemoryUtil.memAllocInt(size).put(indices).flip();
        ibo.bind();
        ibo.upload((long) size * GL46.GL_INT, GL46.GL_DYNAMIC_DRAW);
        ibo.subUpload(buffer, 0);
        mesh.vbos().add(ibo);
        MemoryUtil.memFree(buffer);
      }
      mesh.unbind();
      result.addMesh(mesh);
    });
    return result;
  }
}
