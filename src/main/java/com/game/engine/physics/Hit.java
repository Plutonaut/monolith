package com.game.engine.physics;

import com.game.engine.scene.entities.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class Hit {
  protected Ray ray;
  protected Entity entity;
}
