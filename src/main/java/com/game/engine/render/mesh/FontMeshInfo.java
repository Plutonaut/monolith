package com.game.engine.render.mesh;

import com.game.caches.GlobalCache;
import com.game.graphics.materials.Material;
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

    if (material != null) {
      Material material = GlobalCache.instance().material(this.material);
      mesh.material(material);
    }
    return mesh;
  }
}
