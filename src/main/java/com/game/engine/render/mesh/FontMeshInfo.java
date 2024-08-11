package com.game.engine.render.mesh;

import com.game.engine.physics.Bounds3D;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.utils.engine.MeshInfoUtils;
import com.game.utils.enums.EAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;

@Accessors(fluent = true)
@Getter
@Setter
public class FontMeshInfo extends MeshInfo {
  protected Font font;
  protected String text;
  public FontMeshInfo(String name, String text, Font font) {
    super(name);

    this.font = font;
    this.text = text;
  }

  @Override
  public DynamicMesh create() {
    DynamicMesh mesh = new DynamicMesh(name);
    mesh.vertexCount(vertexCount);
    mesh.isComplex = !indices.isEmpty();
    mesh.material(material);
    VertexInfo positions = getVerticesByAttribute(EAttribute.POS.getValue());
    if (positions != null) {
      Bounds3D bounds = MeshInfoUtils.calculateBounds(positions);
      mesh.updateBounds(bounds.min(), bounds.max());
    }
    return mesh;
  }
}
