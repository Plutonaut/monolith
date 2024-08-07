package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.render.models.Model;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.application.values.ValueMap;
import com.game.utils.engine.FontInfoUtils;
import com.game.utils.engine.loaders.FontResourceLoaderUtils;

import java.awt.*;

public class TextModelGenerator extends AbstractModelGenerator {
  @Override
  public Model generateModel(ValueMap map) {
    String id = map.get("id");
    Model model = new Model(id);
    FontMeshInfo meshInfo = (FontMeshInfo) GlobalCache
      .instance()
      .meshInfo(id, meshInfoName -> generateMeshInfo(map));
    model.addMeshData(meshInfo.name());
    return model;
  }

  public FontMeshInfo generateMeshInfo(ValueMap map) {
    Font font = GlobalCache.instance().getFont(map.get("fontName"), map.getInt("fontSize"));
    String id = map.get("id");
    String text = map.get("text");
    boolean antiAlias = map.getBool("antiAlias");
    Color color = map.getColor("fontColor");
    Color materialColor = map.getColor("diffuseColor");

    FontInfo info = GlobalCache
      .instance()
      .fontInfo(font.getFontName(), n -> FontInfoUtils.process(font, antiAlias, color));
    return FontResourceLoaderUtils.build(id, text, materialColor, info);
  }
}
