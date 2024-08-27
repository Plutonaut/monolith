package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.models.Model;
import com.game.graphics.materials.Material;
import com.game.utils.application.values.ValueMap;
import com.game.utils.enums.EMaterialColor;
import org.lwjgl.opengl.GL46;

import java.awt.*;

public class BillboardModelGenerator extends AbstractModelGenerator {
  @Override
  public Model generateModel(ValueMap data) {
    float[] positions = data.getFloatArr("positions");
    String id = data.get("id");
    String path = data.get("path");
    Color color = data.getColor("diffuseColor");
    Model model = new Model(id);
    MeshInfo meshInfo = GlobalCache.instance().meshInfo(
      id,
      (name) -> new MeshInfoBuilder()
        .use(name)
        .positions(positions)
        .drawMode(GL46.GL_POINTS)
        .material(new Material("trees_mat"))
        .materialColor(EMaterialColor.DIF.getValue(), color)
        .materialDiffuseTexture(path)
        .build()
    );
    model.addMeshData(meshInfo.name());

    return model;
  }
}
