package com.game.utils.engine.loaders;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.engine.render.models.Model;
import com.game.graphics.fonts.CharInfo;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.application.ValueStore;
import com.game.utils.engine.ColorUtils;
import com.game.utils.engine.FontInfoUtils;

import java.awt.*;

public class FontResourceLoader {
  static final float Z_POS = 0.0f;
  static final int VERTICES_PER_QUAD = 4;

  public static Model load(String name, String text, boolean antiAlias, Font font, Color color) {
    Model model = new Model(name + "_model");
    FontMeshInfo meshInfo = (FontMeshInfo) GlobalCache
      .instance()
      .meshInfo(name, n -> build(name, text, antiAlias, font, color));
    model.addMeshData(meshInfo.name());
    return model;
  }

  static FontMeshInfo build(String name, String text, boolean antiAlias, Font font, Color color) {
    FontInfo info = GlobalCache
      .instance()
      .fontInfo(font.getFontName(), n -> FontInfoUtils.process(font, antiAlias, color));
    ValueStore positions = new ValueStore();
    ValueStore textureCoordinates = new ValueStore();
    ValueStore indices = new ValueStore();
    char[] characters = text.toCharArray();
    int numChars = characters.length;

    float startx = 0;
    float starty = 0;
    for (int i = 0; i < numChars; i++) {
      final char c = characters[i];
      final CharInfo charInfo = info.charInfo(c);

      // Build a character tile composed by two triangles
      final float textureCoordX = (float) charInfo.startX() / info.width();
      final float textureCoordX2 = (float) (charInfo.startX() + charInfo.width()) / info.width();

      /*
       * [IF TEXT IS DISPLAYING UPSIDE DOWN] In the Texture class, we are calling
       * STBImage.stbi_set_flip_vertically_on_load(true); When this isn't called, the
       * skybox texture appears upside down.
       *
       * [TEMPORARY FIXES] 1. Swap left top vertex y position with left bottom vertex
       * y position. Do the same for the right vertices. 2. Remove STBImage flip
       * vertically call from the static Texture load(ByteBuffer buffer) method.
       */

      // Left Top vertex
      positions.add(startx); // x
      positions.add(starty + info.height()); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX);
      textureCoordinates.add(0.0f);

      indices.add(i * VERTICES_PER_QUAD);

      // Left Bottom vertex
      positions.add(startx); // x
      positions.add(starty); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX);
      textureCoordinates.add(1.0f);

      indices.add(i * VERTICES_PER_QUAD + 1);

      // Right Bottom vertex
      positions.add(startx + charInfo.width()); // x
      positions.add(starty); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX2);
      textureCoordinates.add(1.0f);

      indices.add(i * VERTICES_PER_QUAD + 2);

      // Right Top vertex
      positions.add(startx + charInfo.width()); // x
      positions.add(starty + info.height()); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX2);
      textureCoordinates.add(0.0f);

      indices.add(i * VERTICES_PER_QUAD + 3);

      // Add indices for left top and bottom right vertices
      indices.add(i * VERTICES_PER_QUAD);
      indices.add(i * VERTICES_PER_QUAD + 2);

      if (c == '\n') {
        startx = 0;
        starty += info.height();
      } else startx += charInfo.width();
    }

    MeshInfoBuilder meshInfoBuilder = new MeshInfoBuilder();
    meshInfoBuilder
      .use(name)
      .positions(positions)
      .textureCoordinates(textureCoordinates)
      .indices(indices)
      .material(name + "_mat")
      .materialDiffuseColor(ColorUtils.convert(color))
      .materialDiffuseTexture(info.texture().path());

    return meshInfoBuilder.build(text);
  }
}
