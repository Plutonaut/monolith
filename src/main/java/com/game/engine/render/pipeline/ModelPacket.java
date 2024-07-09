package com.game.engine.render.pipeline;

import com.game.engine.render.models.Model;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public class ModelPacket implements IRenderPacket {
  protected ERenderer destination;
  protected Model model;
  protected String name;

  public ModelPacket(String name, Model model, ERenderer destination) {
    this.name = name;
    this.model = model;
    this.destination = destination;
  }
}
