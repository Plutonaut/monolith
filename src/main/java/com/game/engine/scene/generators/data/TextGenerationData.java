package com.game.engine.scene.generators.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class TextGenerationData extends AbstractGenerationData {
  protected Font font;
  protected Color color;
  protected Color materialColor;
  protected boolean antiAlias;
  protected String text;

  public TextGenerationData(String id, Font font, Color color, Color materialColor, boolean antiAlias, String... text) {
    super(id);
    this.font = font;
    this.color = color;
    this.materialColor = materialColor;
    this.antiAlias = antiAlias;
    this.text = Arrays.stream(text).reduce(String::concat).orElse("");
  }
}
