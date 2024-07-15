package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;

@Accessors(fluent = true)
@Getter
public enum ESprite {
  IGGY("iggy",
       "sprites%sfield_day%scharacters%siggy%ssprite_atlas.yml".formatted(File.separator,
                                                                          File.separator,
                                                                          File.separator,
                                                                          File.separator
       ),
       false
  );

  private final String atlasName;
  private final String path;
  private final boolean animated;

  ESprite(String atlasName, String path, boolean animated) {
    this.atlasName = atlasName;
    this.path = path;
    this.animated = animated;
  }
}
