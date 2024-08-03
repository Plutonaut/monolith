package com.game.graphics.fonts;

import com.game.engine.render.models.IModel;
import com.game.graphics.texture.Texture;
import com.game.utils.enums.ECache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class FontInfo implements IModel {
  private final Texture texture;
  private final HashMap<Character, CharInfo> characters;
  private final Font font;
  private final int width;
  private final int height;

  public FontInfo(Font font, Texture texture, HashMap<Character, CharInfo> characters, int width, int height) {
    this.font = font;
    this.texture = texture;
    this.characters = characters;
    this.width = width;
    this.height = height;
  }

  @Override
  public String name() { return font.getFontName(); }

  @Override
  public ECache type() { return ECache.FONT_INFO; }

  public CharInfo charInfo(char c) {
    return characters.get(c);
  }
}
