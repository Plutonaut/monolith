package com.game.engine.scene.generators;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.mesh.definitions.Quad;
import com.game.engine.render.models.Model;
import com.game.engine.scene.sprites.Sprite;
import com.game.engine.scene.sprites.SpriteAtlas;
import com.game.engine.scene.sprites.SpriteAtlasRecord;
import com.game.graphics.materials.Material;
import com.game.graphics.texture.Texture;
import com.game.utils.application.LoaderUtils;
import com.game.utils.application.PathSanitizer;
import com.game.utils.application.values.ValueMap;
import com.game.utils.engine.loaders.TextureLoader;
import com.game.utils.enums.EMaterialTexture;
import org.lwjgl.opengl.GL46;

import java.io.File;

public class SpriteResourceModelGenerator extends AbstractModelGenerator {
  protected MeshInfo generateMeshInfo(ValueMap map) {
    return generateMeshInfo(map.get("id"), map.get("path"), map.getBool("clamped"));
  }

  @Override
  public Model generateModel(ValueMap map) {
    if (map.get("strategy").equals("atlas"))
      return generateFromSpriteAtlas(map.get("path"), map.getBool("animated"));

    String id = map.get("id");
    Model model = new Model(id);
    MeshInfo meshInfo = generateMeshInfo(map);
    model.addMeshData(meshInfo.name());
    return model;
  }

  MeshInfo generateMeshInfo(String id, String path, boolean clamped) {
    Material material = new Material(id + "_mat");
    Texture texture = GlobalCache.instance().texture(path, p ->
      TextureLoader.load(p, (texturePath, width, height, buffer) -> {
        Texture t = new Texture(texturePath, width, height);
        t.bind();
        t.store();
        t.nearest();
        if (clamped)
          t.clampBorder(); // TODO: Add more texture wrap options to the SpriteGenerationData object.
        t.upload(GL46.GL_RGBA8, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);
        return t;
      })
    );
    material.texture(EMaterialTexture.DIF.value(), texture.path());
    MeshInfo meshInfo = new Quad(path, texture.width(), texture.height()).meshInfo();
    meshInfo.material(material);

    return meshInfo;
  }

  Model generateFromSpriteAtlas(String path, boolean animated) {
    SpriteAtlas atlas = GlobalCache.instance().spriteAtlas(path);
    SpriteAtlasRecord profile = atlas.get();
    Sprite sprite = profile.get();
    String spritePath = LoaderUtils.getParentDirectory(path) + File.separator + sprite.path();
    String atlasName = atlas.name();
    MeshInfo meshInfo = generateMeshInfo(
      atlasName,
      PathSanitizer.sanitizeFilePath(spritePath),
      true
    );
    Model model = new Model(atlasName);
    model.addMeshData(meshInfo.name());
    if (animated) model.addAnimations(atlas.animations());
    return model;
  }
}
