package com.game.engine.compute;

import com.game.caches.GlobalCache;
import com.game.engine.render.mesh.vertices.ShaderStorageBufferObject;
import com.game.graphics.texture.Texture;
import com.game.utils.application.values.ValueMap;
import com.game.utils.application.values.ValueStore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL46;

@Accessors(fluent = true)
@Data
public class Instruction {
  protected final ShaderStorageBufferObject ssbo;
  protected final ValueMap uniforms;
  protected final ValueStore data;
  protected final Texture frame;
  protected final String name;
  protected boolean ready;

  public Instruction(String name, int binding, int width, int height) {
    this.ssbo = new ShaderStorageBufferObject(binding);
    this.name = name;
    this.ready = false;

    data = new ValueStore();
    uniforms = new ValueMap();
    frame = GlobalCache.instance().texture(name + ".png", (n) -> new Texture(n, width, height));
  }

  public void init(int size) {
    data.fill(size);

    ssbo.bind();
    ssbo.storage(data.asArray());
    ssbo.base();

    data.clear();

    frame.active(0);
    frame.bind();
    frame.repeat();
    frame.nearest();
    frame.storage(1, GL46.GL_RGBA32F);
  }

  public void record(float... data) {
    this.data.add(data);
    this.ready = true;
  }

  public boolean read() {
    if (!ready) return false;
    ssbo.bind();
    ssbo.subUpload(data.asArray(), 0);
    ssbo.base();
    data.clear();
    ready = false;
    return true;
  }

  public void write(int index) {
    frame.active(index);
    frame.write(0, GL46.GL_RGBA32F);
  }

  public void bind() {
    ssbo.bind();
  }

  public void compute(int xWorkers, int yWorkers) {
    GL46.glDispatchCompute(xWorkers, yWorkers, 1);
    GL46.glMemoryBarrier(GL46.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
  }

  public void dispose() {
    ssbo.dispose();
    frame.dispose();
  }
}
