package com.game.loaders;

import java.nio.ByteBuffer;

public interface ITextureBufferReader {
  void read(ByteBuffer buffer, int width, int height);
}
