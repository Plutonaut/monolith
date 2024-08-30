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
  TREE_A("src/main/resources/models/blender/environment/treeA/tree_1A_fix.obj", false),
  RAILWAY_PART("src/main/resources/models/blender/railway_part/railway_part.obj", false),
  ROLY_POLY_BLACK("src/main/resources/models/blender/roly_polys/black/roly_poly.ob", false),
  ROLY_POLY_RED("src/main/resources/models/blender/roly_polys/red/roly_poly.ob", false),
  ROLY_POLY_WHITE("src/main/resources/models/blender/roly_polys/white/roly_poly.ob", false),
  BOB("src/main/resources/models/blender/bob/bob_lamp_update.md5mesh", true);

  private final String path;
  private final boolean animated;

  EModel(String path, boolean animated) {
    this.path = path;
    this.animated = animated;
  }
}
