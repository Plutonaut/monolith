package com.game.engine.render.pipeline.packets;

import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.models.Model;
import com.game.engine.scene.entities.TextEntity;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
public class TextPacketResult implements PacketResult {
  private String text;
  private Mesh mesh;
  private int positionVboId;
  private int textureCoordinateVboId;
  private int indexVboId;

  @Override
  public TextEntity create(Model model) {
    TextEntity gameText = new TextEntity(model.name(), mesh, text);
    gameText.positionVboId(positionVboId);
    gameText.textureCoordinateVboId(textureCoordinateVboId);
    gameText.indexVboId(indexVboId);
    return gameText;
  }

  @Override
  public void addMesh(Mesh mesh) {
    this.mesh = mesh;
  }
}
