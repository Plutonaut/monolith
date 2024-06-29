package com.game.caches;

import com.game.caches.audio.AudioBufferCache;
import com.game.caches.graphics.*;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.MeshInfo;
import com.game.engine.scene.entities.animations.audio.AudioBufferObject;
import com.game.graphics.IGraphicsCachable;
import com.game.graphics.shaders.Program;
import com.game.graphics.shaders.Shader;
import com.game.graphics.texture.Texture;
import com.game.utils.enums.EGraphicsCache;

import java.util.HashMap;

public class GlobalCache {
  private static GlobalCache CACHE;
  private final HashMap<EGraphicsCache, AbstractGraphicsCache> graphicsCache;
  private final HashMap<String, MeshInfo> meshInfoCache;

  private GlobalCache() {
    graphicsCache = new HashMap<>();
    meshInfoCache = new HashMap<>();
  }

  public synchronized static GlobalCache instance() {
    if (CACHE == null) CACHE = new GlobalCache();
    return CACHE;
  }

  public Mesh mesh(String key) {
    return (Mesh) getItem(key, EGraphicsCache.MESH);
  }

  public MeshInfo meshInfo(String key) {
    return meshInfoCache.getOrDefault(key, null);
  }

  public void meshInfo(MeshInfo info) {
    meshInfoCache.putIfAbsent(info.name(), info);
  }

  public Texture texture(String key) {
    return (Texture) getItem(key, EGraphicsCache.TEXTURE);
  }

  public Program program(String key) {return (Program) getItem(key, EGraphicsCache.PROGRAM);}

  public Shader shader(String key) { return (Shader) getItem(key, EGraphicsCache.SHADER); }

  public AudioBufferObject audioBuffer(String key) {return (AudioBufferObject) getItem(key, EGraphicsCache.AUDIO);}

  protected IGraphicsCachable getItem(String key, EGraphicsCache type) {
    return getCache(type).use(key);
  }

  public void cacheItem(IGraphicsCachable item) {getCache(item.type()).cache(item);}

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
