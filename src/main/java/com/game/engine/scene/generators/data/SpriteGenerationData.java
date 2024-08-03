package com.game.engine.scene.generators.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class SpriteGenerationData extends ResourceGenerationData {
  protected boolean clamped;

  public SpriteGenerationData(
    String id,
    String path,
    boolean animated,
    boolean clamped
  ) {
    super(id, path, animated);
    this.clamped = clamped;
  }

  public boolean useAtlas() {
    return path.endsWith(".yml");
  }
}
