package com.game.engine.render.renderers;

import com.game.caches.GlobalCache;
import com.game.engine.render.IRenderable;
import com.game.engine.render.mesh.Mesh;
import com.game.engine.render.mesh.vertices.VertexInfo;
import com.game.engine.scene.Scene;
import com.game.engine.scene.entities.Entity;
import com.game.graphics.materials.MaterialTexturePack;
import com.game.graphics.shaders.Program;
import com.game.graphics.texture.Texture;
import com.game.utils.application.LambdaCounter;
import com.game.utils.enums.EMaterialTexture;
import com.game.utils.enums.ERenderer;
import com.game.utils.enums.EUniform;
import org.lwjgl.opengl.GL46;

import java.util.concurrent.ArrayBlockingQueue;

public abstract class AbstractRenderer implements IRenderer {
  protected final Program program;

  public AbstractRenderer() {
    this.program = GlobalCache.instance().program(type().key());
  }

  public abstract ERenderer type();

  protected abstract void render(IRenderable item, Scene scene);

  public void render(Scene scene) {
    ArrayBlockingQueue<Entity> queue = scene.subQueue(type());

    if (queue.isEmpty()) return;

    program.bind();
    while (queue.peek() != null) render(queue.poll(), scene);
    program.unbind();
  }

  protected void draw(Mesh mesh) {
    mesh.bind();
    mesh.info().vertices().forEach(info -> handleVertexInfo(info, program::enableAttribute));
    draw(mesh, GL46.GL_TRIANGLES);
    mesh.info().vertices().forEach(info -> handleVertexInfo(info, program::disableAttribute));
    mesh.unbind();
  }

  protected void draw(Mesh mesh, int mode) {
    mesh.draw(mode);
  }

  protected void setMaterialTextureUniform(MaterialTexturePack textures) {
    program.uniforms().set(EUniform.MATERIAL_HAS_TEXTURE.value(), textures.hasTexture(EMaterialTexture.DIF.getValue()));
    program.uniforms()
           .set(EUniform.MATERIAL_HAS_NORMAL_MAP.value(), textures.hasTexture(EMaterialTexture.NRM.getValue()));

    LambdaCounter counter = new LambdaCounter();
    textures.pack().forEach((type, path) -> {
      Texture texture = GlobalCache.instance().texture(path);
      String uniform = EMaterialTexture.getUniformByType(type).value();

      if (texture != null && program.hasUniform(uniform)) {
        int index = counter.inc();
        int glIndex = GL46.GL_TEXTURE0 + index;
        texture.active(glIndex);
        texture.bind();

        program.uniforms().set(uniform, index);
      }
    });
  }

  protected void handleVertexInfo(VertexInfo info, IAttribInfoHandler handler) {
    info.attributes().keySet().stream().filter(program::hasAttribute).forEach(handler::handle);
  }

  public void dispose() {
    program.dispose();
  }

  public interface IAttribInfoHandler {
    void handle(String key);
  }
}
