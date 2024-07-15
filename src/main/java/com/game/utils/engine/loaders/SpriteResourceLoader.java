package com.game.utils.engine.loaders;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.engine.render.models.Model;
import com.game.engine.scene.sprites.Sprite;
import com.game.engine.scene.sprites.SpriteAtlas;
import com.game.engine.scene.sprites.SpriteAtlasRecord;
import com.game.graphics.materials.Material;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.PathSanitizer;
import com.game.utils.enums.EMaterialTexture;

import java.io.File;

public class SpriteResourceLoader {
  public static Model load(String path, boolean animated) {
    return GlobalCache.instance().model(path, p -> loadModel(p, animated));
  }

  static Model loadModel(String path, boolean animated) {
    SpriteAtlas atlas = GlobalCache.instance().spriteAtlas(path);
    SpriteAtlasRecord profile = atlas.get();
    Sprite sprite = profile.get();
    String spritePath = LoaderUtils.getParentDirectory(path) + File.separator + sprite.path();
    Material material = GlobalCache.instance().material(path + "_mat");
    material.texture(EMaterialTexture.DIF.getValue(), PathSanitizer.sanitizeFilePath(spritePath));
    MeshInfo meshInfo = new Quad().createMeshInfo();
    meshInfo.material(material.name());
    Model model = new Model(atlas.name());
    model.addMeshData(meshInfo.name());
    if (animated) model.addAnimations(atlas.animations());
    return model;
  }
}
