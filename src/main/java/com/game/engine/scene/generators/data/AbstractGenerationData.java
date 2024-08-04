package com.game.engine.scene.generators.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public abstract class AbstractGenerationData {
  protected String id;
}
