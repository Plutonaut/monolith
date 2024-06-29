package com.game.engine.scene.entities;

import com.game.engine.scene.entities.transforms.ModelTransform;
import com.game.utils.enums.ERenderer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Data
public class EntityDetails {
  protected final String name;
  protected String path;
  protected boolean animated;
  protected ModelTransform transform;
  protected ERenderer shader;
}
