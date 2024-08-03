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
public class ResourceGenerationData extends AbstractGenerationData {
  protected String path;
  protected boolean animated;

  public ResourceGenerationData(String id, String path, boolean animated) {
    super(id);
    this.path = path;
    this.animated = animated;
  }
}
