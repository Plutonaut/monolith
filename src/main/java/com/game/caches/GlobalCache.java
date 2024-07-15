package com.game.caches;

import com.game.caches.audio.AudioBufferCache;
import com.game.caches.graphics.*;
import com.game.caches.models.*;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.audio.AudioBufferObject;
import com.game.engine.scene.sprites.SpriteAtlas;
import com.game.graphics.fonts.FontInfo;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.graphics.texture.Texture;
import com.game.utils.enums.EFont;
import com.game.utils.enums.EGraphicsCache;
import com.game.utils.enums.EModelCache;

import java.awt.*;
import java.util.HashMap;
import java.util.stream.Stream;

public class GlobalCache {
  private static GlobalCache CACHE;
  private final HashMap<EGraphicsCache, AbstractGraphicsCache> graphicsCache;
  private final HashMap<EModelCache, AbstractModelCache> modelCache;
  private final EntityNameResolver entityNameResolver;
  private final FontGraphicsHandler fontGraphicsHandler;

  private GlobalCache() {
    graphicsCache = new HashMap<>();
    modelCache = new HashMap<>();
    entityNameResolver = new EntityNameResolver();
    fontGraphicsHandler = new FontGraphicsHandler();
  }

  public synchronized static GlobalCache instance() {
    if (CACHE == null) CACHE = new GlobalCache();
    return CACHE;
  }

  public Font getFont(EFont font, int fontSize) {
    return getFont(font.value(), fontSize);
  }

  public Font getFont(String fontName, int fontSize) {
    return fontGraphicsHandler.getFont(fontName, fontSize);
  }

  public String resolveEntityName(String entityName) {
    return entityNameResolver.getAvailable(entityName);
  }

  public Stream<String> matchingEntityNames(String entityName) {
    return entityNameResolver.matchingNames(entityName);
  }

  public void removeEntityName(String entityName) {
    entityNameResolver.removeName(entityName);
  }

  public SpriteAtlas spriteAtlas(
    String name,
    IModelGenerator generator
  ) { return (SpriteAtlas) getItem(name, EModelCache.SPRITE_ATLAS, generator); }

  public SpriteAtlas spriteAtlas(String name) {
    return (SpriteAtlas) getItem(
      name,
      EModelCache.SPRITE_ATLAS
    );
  }

  public FontInfo fontInfo(String name, IModelGenerator generator) {
    return (FontInfo) getItem(name, EModelCache.FONT_INFO, generator);
  }

  public FontInfo fontInfo(String name) {
    return (FontInfo) getItem(name, EModelCache.FONT_INFO);
  }

  public MeshInfo meshInfo(String name, IModelGenerator generator) {
    return (MeshInfo) getItem(name, EModelCache.MESH_INFO, generator);
  }

  public MeshInfo meshInfo(String name) {
    return (MeshInfo) getItem(name, EModelCache.MESH_INFO);
  }

  public Material material(String name) {
    return (Material) getItem(name, EModelCache.MATERIAL);
  }

  public Model model(String name, IModelGenerator generator) {
    return (Model) getItem(name, EModelCache.MODEL, generator);
  }

  public Model model(String name) {
    return (Model) getItem(name, EModelCache.MODEL);
  }

  public IModelCachable getItem(String name, EModelCache type, IModelGenerator generator) {
    return getCache(type).use(name, generator);
  }

  public IModelCachable getItem(String name, EModelCache type) { return getCache(type).use(name); }

  public void cacheItem(IModelCachable model) { getCache(model.type()).cache(model); }

  protected AbstractModelCache getCache(EModelCache type) {
    return modelCache.computeIfAbsent(type, t -> switch (t) {
      case MODEL -> new ModelCache();
      case MATERIAL -> new MaterialCache();
      case MESH_INFO -> new MeshInfoCache();
      case SPRITE_ATLAS -> new SpriteAtlasCache();
      case FONT_INFO -> new FontInfoCache();
    });
  }

  public Mesh mesh(String key) {
    return (Mesh) getItem(key, EGraphicsCache.MESH);
  }

  public Texture texture(String key) {
    return (Texture) getItem(key, EGraphicsCache.TEXTURE);
  }

  public Texture texture(String key, IGraphicsGenerator generator) {
    return (Texture) getItem(key, EGraphicsCache.TEXTURE, generator);
  }

  public Program program(String key) { return (Program) getItem(key, EGraphicsCache.PROGRAM); }

  public Shader shader(String key) { return (Shader) getItem(key, EGraphicsCache.SHADER); }

  public AudioBufferObject audioBuffer(String key) {
    return (AudioBufferObject) getItem(
      key,
      EGraphicsCache.AUDIO
    );
  }

  protected IGraphicsCachable getItem(String key, EGraphicsCache type) {
    return getCache(type).use(key);
  }

  protected IGraphicsCachable getItem(
    String key,
    EGraphicsCache type,
    IGraphicsGenerator generator
  ) {
    return getCache(type).use(key, generator);
  }

  public void cacheItem(IGraphicsCachable item) { getCache(item.type()).cache(item); }

  protected AbstractGraphicsCache getCache(EGraphicsCache type) {
    return graphicsCache.computeIfAbsent(type, t -> switch (t) {
      case MESH -> new MeshCache();
      case TEXTURE -> new TextureCache();
      case SHADER -> new ShaderCache();
      case PROGRAM -> new ProgramCache();
      case AUDIO -> new AudioBufferCache();
    });
  }

  public void dispose() {
    graphicsCache.forEach((k, v) -> v.dispose());
  }
}
