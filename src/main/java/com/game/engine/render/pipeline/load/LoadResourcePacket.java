package com.game.engine.render.pipeline.load;

import com.game.engine.render.pipeline.IRenderPacket;
import com.game.utils.enums.ERenderer;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
public class LoadResourcePacket implements IRenderPacket {
  protected String name;
  protected String path;
  protected boolean animated;
  protected ERenderer destination;

  public LoadResourcePacket(String name, String path, boolean animated, ERenderer destination) {
    this.name = name;
    this.path = path;
    this.animated = animated;
    this.destination = destination;
  }
}
