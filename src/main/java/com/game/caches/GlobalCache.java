package com.game.caches;

import com.game.caches.audio.AudioBufferCache;
import com.game.caches.graphics.AbstractGraphicsCache;
import com.game.caches.graphics.concrete.MeshCache;
import com.game.caches.graphics.concrete.ProgramCache;
import com.game.caches.graphics.concrete.ShaderCache;
import com.game.caches.graphics.concrete.TextureCache;
import com.game.caches.graphics.interfaces.IGraphicsCachable;
import com.game.caches.graphics.interfaces.IGraphicsGenerator;
import com.game.caches.models.AbstractModelCache;
import com.game.caches.models.concrete.*;
import com.game.caches.models.interfaces.IModelCachable;
import com.game.caches.models.interfaces.IModelGenerator;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.render.models.Model;
import com.game.engine.scene.audio.AudioBufferObject;
import com.game.engine.scene.entities.EntityManager;
import com.game.engine.scene.sprites.SpriteAtlas;
import com.game.graphics.fonts.FontInfo;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.graphics.texture.Texture;
import com.game.utils.application.ValueStore2D;
import com.game.utils.engine.FontInfoUtils;
import com.game.utils.engine.ModelUtils;
import com.game.utils.engine.loaders.TextureLoader;
import com.game.utils.enums.EGraphicsCache;
import com.game.utils.enums.EModelCache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class GlobalCache {
  private static GlobalCache CACHE;
  private final HashMap<EGraphicsCache, AbstractGraphicsCache> graphicsCache;
  private final HashMap<EModelCache, AbstractModelCache> modelCache;
  private final FontGraphicsHandler fontGraphicsHandler;
  private final EntityManager entityManager;

  private GlobalCache() {
    graphicsCache = new HashMap<>();
    modelCache = new HashMap<>();
    entityManager = new EntityManager();
    fontGraphicsHandler = new FontGraphicsHandler();
  }

  public synchronized static GlobalCache instance() {
    if (CACHE == null) CACHE = new GlobalCache();
    return CACHE;
  }

  // TODO: Move to a FontFileLoader class.
  public Font getFont(String fontName, int fontSize) {
    return fontGraphicsHandler.getFont(fontName, fontSize);
  }

  public String resolveEntityName(String entityName) {
    return entityManager.getAvailableEntityName(entityName);
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

  public FontInfo fontInfo(String name) {
    return fontInfo(name, FontInfoUtils::process);
  }

  public FontInfo fontInfo(String name, IModelGenerator generator) {
    return (FontInfo) getItem(name, EModelCache.FONT_INFO, generator);
  }

  public MeshInfo meshInfo(String name, IModelGenerator generator) {
    String meshInfoName = ModelUtils.resolveMeshInfoName(name);
    return (MeshInfo) getItem(meshInfoName, EModelCache.MESH_INFO, generator);
  }

  public MeshInfo meshInfo(String name) {
    return meshInfo(name, MeshInfo::new);
  }

  public Material material(String name) {
    return (Material) getItem(name, EModelCache.MATERIAL);
  }

  public Model model(String name, IModelGenerator generator) {
    String modelName = ModelUtils.resolveModelName(name);
    return (Model) getItem(modelName, EModelCache.MODEL, generator);
  }

  public Model model(String name) {
    return model(name, Model::new);
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
    return texture(key, TextureLoader::load);
  }

  public Texture texture(String path, ByteBuffer buffer) {
    return texture(path, (p) -> TextureLoader.load(p, buffer));
  }

  public Texture texture(String path, ValueStore2D grid) {
    return texture(path, (p) -> TextureLoader.load(p, grid));
  }

  public Texture texture(String path, BufferedImage image, boolean save) {
    return texture(path, (p) -> TextureLoader.load(p, image, save));
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
