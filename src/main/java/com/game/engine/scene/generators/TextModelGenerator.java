package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.generators.data.TextGenerationData;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.engine.FontInfoUtils;
import com.game.utils.engine.loaders.FontResourceLoaderUtils;

import java.awt.*;

public class TextModelGenerator extends AbstractModelGenerator<TextGenerationData> {
  @Override
  public Model generateModel(TextGenerationData data) {
    String id = data.id();
    Model model = new Model(id);
    FontMeshInfo meshInfo = (FontMeshInfo) GlobalCache
      .instance()
      .meshInfo(id, meshInfoName -> generateMeshInfo(data));
    model.addMeshData(meshInfo.name());
    return model;
  }

  public FontMeshInfo generateMeshInfo(TextGenerationData data) {
    Font font = data.font();
    String id = data.id();
    String text = data.text();
    boolean antiAlias = data.antiAlias();
    Color color = data.color();
    Color materialColor = data.materialColor();

    FontInfo info = GlobalCache
      .instance()
      .fontInfo(font.getFontName(), n -> FontInfoUtils.process(font, antiAlias, color));
    return FontResourceLoaderUtils.build(id, text, materialColor, info);
  }
}
