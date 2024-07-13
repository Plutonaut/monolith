package com.game.engine.render.pipeline.packets;

import com.game.utils.enums.ERenderer;

public interface IRenderPacket {
  ERenderer destination();
}
