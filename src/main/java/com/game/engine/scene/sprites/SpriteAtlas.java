package com.game.engine.scene.sprites;

import com.game.caches.GlobalCache;
import com.game.caches.models.interfaces.IModelCachable;
import com.game.engine.scene.entities.animations.Animation2D;
import com.game.graphics.texture.Texture;
import com.game.utils.application.PathSanitizer;
import com.game.utils.enums.EModelCache;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Accessors(fluent = true)
@NoArgsConstructor
@Slf4j
@Data
public class SpriteAtlas implements IModelCachable {
  protected String name;
  protected List<SpriteAtlasRecord> atlas;

  public SpriteAtlas(String name, List<SpriteAtlasRecord> atlas) {
    this.name = name;
    this.atlas = atlas;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAtlas(List<SpriteAtlasRecord> atlas) { this.atlas = atlas; }

  public static SpriteAtlas load(String path) {
    LoaderOptions options = new LoaderOptions();
    Constructor constructor = new Constructor(SpriteAtlas.class, options);
    Yaml yaml = new Yaml(constructor);
    SpriteAtlas atlas = null;
    try (InputStream in = new FileInputStream(PathSanitizer.sanitizeFilePath(path))) {
      if (in.available() == 0) log.error("Input stream was empty for file {}", path);
      else atlas = yaml.load(in);
    } catch (IOException ex) {
      log.error("Could not load YAML file {}", path);
      throw new RuntimeException(ex);
    }

    return atlas;
  }

  @Override
  public EModelCache type() {
    return EModelCache.SPRITE_ATLAS;
  }

  public SpriteAtlasRecord get() {
    return atlas.getFirst();
  }

  public SpriteAtlasRecord get(String key) {
    return atlas.stream().filter(n -> n.name().equals(key)).findFirst().orElse(null);
  }

  public List<Sprite> sprites(String key) {
    SpriteAtlasRecord record = get(key);
    return record != null ? record.sprites() : new ArrayList<>();
  }

  public List<Animation2D> animations() {
    return atlas
      .stream()
      .filter(SpriteAtlasRecord::animated)
      .map(record -> new Animation2D(record.name(), record.sprites()))
      .toList();
  }

  public List<Texture> textures(String key) {
    return sprites(key).stream().map(sprite -> texture(sprite.path())).collect(Collectors.toList());
  }

  Texture texture(String path) {
    return GlobalCache.instance().texture(path);
  }
}
