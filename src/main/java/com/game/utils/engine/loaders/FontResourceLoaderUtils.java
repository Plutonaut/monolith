package com.game.utils.engine.loaders;

import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.mesh.MeshInfoBuilder;
import com.game.graphics.fonts.CharInfo;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.application.values.ValueStore;
import com.game.utils.engine.ColorUtils;

import java.awt.*;

public class FontResourceLoaderUtils {
  static final float Z_POS = 0.0f;
  static final int VERTICES_PER_QUAD = 4;

  // TODO: Move font information to model, similar to animation data.
  public static FontMeshInfo build(String name, String text, Color color, FontInfo info) {
    ValueStore positions = new ValueStore();
    ValueStore textureCoordinates = new ValueStore();
    ValueStore indices = new ValueStore();
    char[] characters = text.toCharArray();
    int numChars = characters.length;
    int infoWidth = info.width();
    int infoHeight = info.height();

    float startX = 0;
    float startY = 0;
    for (int i = 0; i < numChars; i++) {
      final char c = characters[i];
      final CharInfo charInfo = info.charInfo(c);

      // Build a character tile composed by two triangles
      final float textureCoordX = (float) charInfo.startX() / infoWidth;
      final float textureCoordX2 = (float) (charInfo.startX() + charInfo.width()) / infoWidth;

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
      positions.add(startX); // x
      positions.add(startY + infoHeight); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX);
      textureCoordinates.add(0.0f);

      indices.add(i * VERTICES_PER_QUAD);

      // Left Bottom vertex
      positions.add(startX); // x
      positions.add(startY); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX);
      textureCoordinates.add(1.0f);

      indices.add(i * VERTICES_PER_QUAD + 1);

      // Right Bottom vertex
      positions.add(startX + charInfo.width()); // x
      positions.add(startY); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX2);
      textureCoordinates.add(1.0f);

      indices.add(i * VERTICES_PER_QUAD + 2);

      // Right Top vertex
      positions.add(startX + charInfo.width()); // x
      positions.add(startY + infoHeight); // y
      positions.add(Z_POS); // z

      textureCoordinates.add(textureCoordX2);
      textureCoordinates.add(0.0f);

      indices.add(i * VERTICES_PER_QUAD + 3);

      // Add indices for left top and bottom right vertices
      indices.add(i * VERTICES_PER_QUAD);
      indices.add(i * VERTICES_PER_QUAD + 2);

      if (c == '\n') {
        startX = 0;
        startY += infoHeight;
      } else startX += charInfo.width();
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

    return meshInfoBuilder.build(text, info.font());
  }
}
