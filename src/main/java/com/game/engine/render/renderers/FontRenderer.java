package com.game.engine.render.renderers;

import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.FontMeshInfo;
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
    DynamicMesh mesh = text.mesh();
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
      mesh.redraw(info, v -> mesh.setVertexAttributeArrays(program.attributes().point(v)));
      result.text(fontMeshInfo.text());
      result.font(fontMeshInfo.font());
      result.addMesh(mesh);
    });
    return result;
  }
}
