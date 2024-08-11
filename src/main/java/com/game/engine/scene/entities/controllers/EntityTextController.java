package com.game.engine.scene.entities.controllers;

import com.game.caches.GlobalCache;
import com.game.engine.physics.Bounds2D;
import com.game.engine.render.mesh.DynamicMesh;
import com.game.engine.render.mesh.FontMeshInfo;
import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.graphics.fonts.FontInfo;
import com.game.utils.engine.ColorUtils;
import com.game.utils.engine.loaders.FontResourceLoaderUtils;
import com.game.utils.enums.EController;
import com.game.utils.enums.EMaterialColor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data
public class EntityTextController extends AbstractEntityController {
  protected final Bounds2D bounds;
  protected DynamicMesh mesh;
  protected String text;
  protected Font font;
  protected int borderSize;

  public EntityTextController() {
    bounds = new Bounds2D();
  }

  @Override
  public String type() {
    return EController.TEXT.value();
  }

  @Override
  public void onUpdate(ModelTransform transform) {
    bounds.origin().set(transform.position().x(), transform.position().y());
    updateBounds();
  }

  public void redraw(String text) {
    redraw(text, font);
  }

  public void redraw(String text, Font font) {
    redraw(text, font, Color.white);
  }

  public void setColor() {
    setColor(Color.white);
  }

  public void setColor(Color color) {
    mesh.material().color(EMaterialColor.DIF.getValue(), ColorUtils.convert(color));
  }

  public void redraw(String text, Font font, Color color) {
    if (text == null || this.text.equals(text)) return;
    this.text = text;
    this.font = font;

    FontInfo fontInfo = GlobalCache.instance().fontInfo(font.getFontName());
    FontMeshInfo meshInfo = FontResourceLoaderUtils.build(mesh.key(), text, color, fontInfo);
    mesh.redraw(meshInfo, null);
    updateBounds();
  }

  void updateBounds() {
    bounds.min().set(mesh.min().x(), mesh.min().y());
    bounds.max().set(mesh.max().x(), mesh.max().y());
  }
}
