package com.game.engine.render.pipeline.packets;

import com.game.engine.render.models.Model;
import com.game.utils.enums.ERenderer;

public interface ModelBinder {
  PacketResult bind(ERenderer shader, Model model);
}
