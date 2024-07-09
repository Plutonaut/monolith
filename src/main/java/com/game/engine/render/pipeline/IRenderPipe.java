package com.game.engine.render.pipeline;

public interface IRenderPipe {
  IRenderPipe receive(IRenderPacket packet);
  IRenderPipe pipe(IRenderPipe receiver);
}
