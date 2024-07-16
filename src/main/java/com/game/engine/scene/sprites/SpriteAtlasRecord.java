package com.game.engine.scene.sprites;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@Data
public class SpriteAtlasRecord {
  private String name;
  private boolean animated;
  private List<Sprite> sprites;

  public SpriteAtlasRecord(String name, boolean animated, List<Sprite> sprites) {
    this.name = name;
    this.animated = animated;
    this.sprites = sprites;
  }

  public void setName(String name) { this.name = name; }

  public void setAnimated(boolean animated) { this.animated = animated; }

  public void setSprites(List<Sprite> sprites) { this.sprites = sprites; }

  public Sprite get() { return sprites.getFirst(); }

  public Sprite get(String name) {
    return sprites.stream().filter(s -> s.name().equals(name)).findFirst().orElse(null);
  }
}
