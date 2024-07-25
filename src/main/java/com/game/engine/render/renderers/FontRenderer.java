package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.render.pipeline.packets.TextPacketResult;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.TextEntity;
import com.game.graphics.materials.Material;
import com.game.utils.enums.EProjection;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.joml.Matrix4f;

public class FontRenderer extends AbstractLitRenderer {
  @Override
  public ERenderer type() { return ERenderer.FONT; }

  @Override
  protected void render(IRenderable item, Scene scene) {
    scene.cull(false);
    TextEntity text = (TextEntity) item;
    Matrix4f projection = new Matrix4f()
      .set(scene.projectionMat(EProjection.ORTHOGRAPHIC_FONT_2D))
      .mul(text.transform().worldModelMat());
    program.uniforms().set(EUniform.PROJECTION.value(), projection);
    Mesh mesh = text.mesh();
    Material material = mesh.material();
    setMaterialUniform(material);
    draw(mesh);
    scene.cull(true);
  }

  @Override
  public TextPacketResult associate(Model model) {
    TextPacketResult result = new TextPacketResult();
    model.meshInfo().forEach(info -> {
        FontMeshInfo fontMeshInfo = (FontMeshInfo) info;
      DynamicMesh mesh = fontMeshInfo.create();
//      mesh.bind();
//      info.vertices().forEach(vertex -> {
//        VertexBufferObject vbo = new VertexBufferObject();
//        float[] values = vertex.vertices().asArray();
//        int size = values.length;
//        FloatBuffer buffer = MemoryUtil.memAllocFloat(size).put(values).flip();
//        vbo.bind();
//        vbo.upload((long) size * GL46.GL_FLOAT, GL46.GL_DYNAMIC_DRAW);
//        vbo.subUpload(buffer, 0);
//        mesh.vbos().add(vbo);
//        mesh.setVertexAttributeArrays(program.attributes().point(vertex));
//        MemoryUtil.memFree(buffer);
//      });
//      if (mesh.isComplex()) {
//        IndexBufferObject ibo = new IndexBufferObject();
////        result.indexVboId(ibo.glId());
//        int[] indices = info.indices().asIntArray();
//        int size = indices.length;
//        IntBuffer buffer = MemoryUtil.memAllocInt(size).put(indices).flip();
//        ibo.bind();
//        ibo.upload((long) size * GL46.GL_INT, GL46.GL_DYNAMIC_DRAW);
//        ibo.subUpload(buffer, 0);
//        mesh.vbos().add(ibo);
//        MemoryUtil.memFree(buffer);
//      }
//      mesh.unbind();
      mesh.redraw(info, v -> mesh.setVertexAttributeArrays(program.attributes().point(v)));
      result.text(fontMeshInfo.text());
      result.font(fontMeshInfo.font());
      result.addMesh(mesh);
    });
    return result;
  }
}
