package com.game.loaders;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface ITextureBufferStrategy {
  ByteBuffer load(IntBuffer w, IntBuffer h, IntBuffer avChannels);
}
