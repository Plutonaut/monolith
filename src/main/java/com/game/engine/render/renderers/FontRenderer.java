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
    Matrix4f projection = scene.projectionMat(EProjection.ORTHOGRAPHIC_FONT_2D);
    program.uniforms().set(EUniform.PROJECTION.value(), text.projModelMatrix(projection));
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
      Mesh mesh = info.create();
      mesh.bind();
      info.vertices().forEach(vertex -> {
        VertexBufferObject vbo = new VertexBufferObject();
        int vboId = vbo.glId();
        mesh.vbos().add(vbo);

        if (vertex.hasAttribute(EAttribute.POS.getValue())) result.positionVboId(vboId);
        else if (vertex.hasAttribute(EAttribute.TXC.getValue()))
          result.textureCoordinateVboId(vboId);

        float[] values = vertex.vertices().asArray();
        int size = values.length;
        vbo.bind();
        vbo.upload(size, GL46.GL_DYNAMIC_DRAW);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(size).put(values).flip();
        vbo.subUpload(buffer, 0);
        List<String> vaas = program
          .attributes()
          .point(vertex.attributes().values(), vertex.glType());
        mesh.vaas().addAll(vaas);
      });
      if (mesh.isComplex()) {
        IndexBufferObject ibo = new IndexBufferObject();
        mesh.vbos().add(ibo);
        result.indexVboId(ibo.glId());
        int[] indices = info.indices().asIntArray();
        int size = indices.length;
        ibo.bind();
        ibo.upload(size, GL46.GL_DYNAMIC_DRAW);
        IntBuffer buffer = MemoryUtil.memAllocInt(size).put(indices).flip();
        ibo.subUpload(buffer, 0);
      }
      mesh.unbind();
      result.addMesh(mesh);
    });
    return result;
  }
}
