package com.game.loaders;

import com.game.graphics.texture.Texture;

import java.nio.ByteBuffer;

public interface ITextureBufferCreator {
  Texture create(String path, int width, int height, ByteBuffer buffer);
}
