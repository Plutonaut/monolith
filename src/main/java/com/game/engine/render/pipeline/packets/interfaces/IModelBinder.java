package com.game.engine.render.pipeline.packets.interfaces;

import com.game.engine.render.models.Model;
import com.game.utils.enums.ERenderer;

public interface IModelBinder {
  IPacketResult bind(ERenderer shader, Model model);
}
