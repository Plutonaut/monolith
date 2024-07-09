package com.game.caches;

import com.game.caches.audio.AudioBufferCache;
import com.game.caches.graphics.*;
import com.game.caches.models.*;
import com.game.engine.render.models.Model;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.scene.entities.animations.audio.AudioBufferObject;
import com.game.graphics.materials.Material;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.graphics.texture.Texture;
import com.game.utils.enums.EGraphicsCache;
import com.game.utils.enums.EModelCache;

import java.util.HashMap;

public class GlobalCache {
  private static GlobalCache CACHE;
  private final HashMap<EGraphicsCache, AbstractGraphicsCache> graphicsCache;
  private final HashMap<EModelCache, AbstractModelCache> modelCache;

  private GlobalCache() {
    graphicsCache = new HashMap<>();
    modelCache = new HashMap<>();
  }

  public synchronized static GlobalCache instance() {
    if (CACHE == null) CACHE = new GlobalCache();
    return CACHE;
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
    });
  }

  public Mesh mesh(String key) {
    return (Mesh) getItem(key, EGraphicsCache.MESH);
  }

  public Texture texture(String key) {
    return (Texture) getItem(key, EGraphicsCache.TEXTURE);
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
