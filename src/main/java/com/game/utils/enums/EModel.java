package com.game.utils.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public enum EModel {
  CUBE("src/main/resources/models/cube/cube.obj", false),
  QUAD("src/main/resources/models/quad/quad.obj", false),
  BASIC_SKYBOX("src/main/resources/models/skybox/skybox.obj", false),
  SKYBOX_1("src/main/resources/models/blender/skybox_1/skybox_1.obj", false),
  BLUE_SHAPE("src/main/resources/models/blender/blue_shape/blue_shape.obj", false),
  RAILWAY_PART("src/main/resources/models/blender/railway_part/railway_part.obj", false),
  BOB("src/main/resources/models/blender/bob/bob_lamp_update.md5mesh", true);

  private final String path;
  private final boolean animated;

  EModel(String path, boolean animated) {
    this.path = path;
    this.animated = animated;
  }
}
