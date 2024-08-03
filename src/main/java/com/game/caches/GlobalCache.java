package com.game.caches;

import com.game.caches.audio.AudioBufferCache;
import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.caches.graphics.concrete.MeshCache;
import com.game.caches.graphics.concrete.ProgramCache;
import com.game.caches.graphics.concrete.ShaderCache;
import com.game.caches.graphics.concrete.TextureCache;
import com.game.caches.models.concrete.FontInfoCache;
import com.game.caches.models.concrete.MeshInfoCache;
import com.game.caches.models.concrete.ModelCache;
import com.game.caches.models.concrete.SpriteAtlasCache;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.audio.AudioBufferObject;
import com.game.engine.scene.sprites.SpriteAtlas;
import com.game.graphics.fonts.FontInfo;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.graphics.texture.Texture;
import com.game.utils.application.ValueGrid;
import com.game.utils.engine.FontInfoUtils;
import com.game.utils.engine.ModelUtils;
import com.game.utils.engine.loaders.TextureLoader;
import com.game.utils.engine.logging.DiagnosticLoggingHandler;
import com.game.utils.enums.ECache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class GlobalCache {
  private static GlobalCache CACHE;
  private final HashMap<ECache, AbstractCache> cache;
  private final FontGraphicsHandler fontGraphicsHandler;

  private GlobalCache() {
    cache = new HashMap<>();
    fontGraphicsHandler = new FontGraphicsHandler();
  }

  public synchronized static GlobalCache instance() {
    if (CACHE == null) CACHE = new GlobalCache();
    return CACHE;
  }

  ICachable getItem(String name, ECache type) {
    return getCache(type).use(name);
  }

  ICachable getItem(String name, ECache type, ICachableGenerator generator) {
    return getCache(type).use(name, generator);
  }

  ICachable cacheItem(String key, ECache type, ICachable item) {
    return getCache(type).cache(key, item);
  }

  public void dispose() {
    cache.forEach((k, v) -> {
      if (v instanceof AbstractGraphicsCache graphicsCache)
        graphicsCache.dispose();
    });
  }

  protected AbstractCache getCache(ECache type) {
    return cache.computeIfAbsent(type, t -> switch (t) {
      case MODEL -> new ModelCache();
      case MESH_INFO -> new MeshInfoCache();
      case SPRITE_ATLAS -> new SpriteAtlasCache();
      case FONT_INFO -> new FontInfoCache();
      case MESH -> new MeshCache();
      case TEXTURE -> new TextureCache();
      case SHADER -> new ShaderCache();
      case PROGRAM -> new ProgramCache();
      case AUDIO -> new AudioBufferCache();
    });
  }

  // TODO: Move to a FontFileLoader class.
  public Font getFont(String fontName, int fontSize) {
    return fontGraphicsHandler.getFont(fontName, fontSize);
  }

  public SpriteAtlas spriteAtlas(
    String name,
    ICachableGenerator generator
  ) { return (SpriteAtlas) getItem(name, ECache.SPRITE_ATLAS, generator); }

  public SpriteAtlas spriteAtlas(String name) {
    return (SpriteAtlas) getItem(
      name,
      ECache.SPRITE_ATLAS
    );
  }

  public FontInfo fontInfo(String name) {
    return fontInfo(name, FontInfoUtils::process);
  }

  public FontInfo fontInfo(String name, ICachableGenerator generator) {
    return (FontInfo) getItem(name, ECache.FONT_INFO, generator);
  }

  public MeshInfo meshInfo(String name, ICachableGenerator generator) {
    String meshInfoName = ModelUtils.resolveMeshInfoName(name);
    return (MeshInfo) getItem(meshInfoName, ECache.MESH_INFO, generator);
  }

  public MeshInfo meshInfo(String name) {
    return meshInfo(name, MeshInfo::new);
  }

  public Model model(String name, ICachableGenerator generator) {
    String modelName = ModelUtils.resolveModelName(name);
    return (Model) getItem(modelName, ECache.MODEL, generator);
  }

  public Model model(String name) {
    return model(name, Model::new);
  }

  public Mesh mesh(String key) {
    return (Mesh) getItem(key, ECache.MESH);
  }

  public Mesh mesh(String name, ICachableGenerator generator) {
    return (Mesh) getItem(name, ECache.MESH, generator);
  }

  public Mesh mesh(String key, Mesh mesh) {
    return (Mesh) cacheItem(key, ECache.MESH, mesh);
  }

  public Texture texture(String key) {
    return texture(key, TextureLoader::load);
  }

  public Texture texture(String path, ByteBuffer buffer) {
    return texture(path, (p) -> TextureLoader.load(p, buffer));
  }

  public Texture texture(String path, ValueGrid grid) {
    return texture(path, (p) -> TextureLoader.load(p, grid));
  }

  public Texture texture(String path, BufferedImage image, boolean save) {
    return texture(path, (p) -> TextureLoader.load(p, image, save));
  }

  public Texture texture(String key, ICachableGenerator generator) {
    return (Texture) getItem(key, ECache.TEXTURE, generator);
  }

  public Program program(String key) { return (Program) getItem(key, ECache.PROGRAM); }

  public Shader shader(String key) { return (Shader) getItem(key, ECache.SHADER); }

  public AudioBufferObject audioBuffer(String key) {
    return (AudioBufferObject) getItem(
      key,
      ECache.AUDIO
    );
  }

  public void computeDiagnostics(DiagnosticLoggingHandler handler) {
    handler.open("Global Cache");
    cache.forEach((k, v) -> {
      handler.row(k.name(), "Items: " + v.cache.size());
      v.cache.keySet().forEach(handler::row);
    });
    handler.close();
  }
}
