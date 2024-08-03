package com.game.engine.scene.entities.controllers;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.engine.loaders.FontResourceLoaderUtils;
import com.game.utils.enums.EController;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class EntityTextController extends AbstractEntityController {
  private DynamicMesh mesh;
  private String text;
  private Font font;

  @Override
  public String type() {
    return EController.TEXT.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {

  }

  public void redraw(String text) {
    redraw(text, font);
  }

  public void redraw(String text, Font font) {
    redraw(text, font, Color.white);
  }

  public void redraw(String text, Font font, Color color) {
    if (text == null || this.text.equals(text)) return;
    FontInfo fontInfo = GlobalCache.instance().fontInfo(font.getFontName());
    FontMeshInfo meshInfo = FontResourceLoaderUtils.build(mesh.key(), text, color, fontInfo);
    mesh.redraw(meshInfo, null);
    this.text = text;
    this.font = font;
  }
}
