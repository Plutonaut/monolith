package com.game.loaders;

import java.nio.ByteBuffer;

public interface ITextureReader {
  void read(ByteBuffer buffer, int width, int height);
}
