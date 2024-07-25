package com.game.engine.render.pipeline.packets;

import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.TextEntity;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;

@Accessors(fluent = true)
@Setter
public class TextPacketResult implements PacketResult {
  private String text;
  private Font font;
  private DynamicMesh mesh;

  @Override
  public TextEntity create(Model model) {
    return new TextEntity(model.name(), mesh, text, font);
  }

  @Override
  public void addMesh(Mesh mesh) {
    this.mesh = (DynamicMesh) mesh;
  }
}
