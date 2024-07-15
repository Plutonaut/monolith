package com.game.graphics.fonts;

import com.game.caches.models.IModelCachable;
import com.game.graphics.texture.Texture;
import com.game.utils.enums.EModelCache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.HashMap;

@Accessors(fluent = true)
@Data
public class FontInfo implements IModelCachable {
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
  public EModelCache type() { return EModelCache.FONT_INFO; }

  public CharInfo charInfo(char c) {
    return characters.get(c);
  }
}
